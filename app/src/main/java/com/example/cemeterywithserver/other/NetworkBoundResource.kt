package com.example.cemeterywithserver.other

import kotlinx.coroutines.flow.*
import timber.log.Timber

/*
    inline - means function does not have an address internally to complier. this makes the function more efficient

    ResultType - what local db returns
    RequestType - what network returns. Could be objects could be responses

    This can be used in other projects as caching strategy

    getAllNotes from dao returns Flow<List<Cemetery>> we can pass this as the query lambda
 */

inline fun <ResultType, RequestType> networkBoundResource (
    //query is lambda function that declares the logic for how we want to load something from database
    //must use crossinline for lambda function parameters
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch : suspend () -> RequestType, //network call return RequestType which is network responses
    crossinline saveFetchResult: suspend (RequestType) -> Unit, //saves the fetch data from fetch function
    crossinline onFetchFailed: (Throwable) -> Unit = {Unit},

    //logic for if we should fetch data
    crossinline shouldFetch: (ResultType) -> Boolean = { true } //default value for lambda is true

) = flow {

    //emit is a suspend function and flow is a coroutine so we are able to call suspend functions from within flow
    //when we call emit function live data will trigger later on
    emit(Resource.loading(null)) //no data because the state is loading

    val data = query().first() //first() assigns first elision of data to data(list of cemeteries). Query is defined later on when we define this query lambda (dao method that returns Flow<List<Cemetery>>)

    //if should fetch is true that means we want to fetch our data from our api
    //should fetch will make a network call. We emit the data from database first from the query().first()
    val flow = if(shouldFetch(data)){

        emit(Resource.loading(data))

        try {
            val fetchedResult = fetch() //fetch will be defined as cemetery api's getallCems
            saveFetchResult(fetchedResult) //means we got data from api call and we save it (updated list of cems into local database)
            query().map { Resource.success(it) } //query for local database list and map it as Resource.success data

        }catch (t : Throwable){

            //case for fetch (api call) goes wrong
            Timber.i(t)

            onFetchFailed(t)
            query().map {
                Resource.error("Couldn't reach server", it) //even if fetch failed we still get our data from local database
            }
        }

    }else{
        query().map { Resource.success(it) } //if we should not fetch from network just query local database for list map is to a success
    }

    emitAll(flow)
}


