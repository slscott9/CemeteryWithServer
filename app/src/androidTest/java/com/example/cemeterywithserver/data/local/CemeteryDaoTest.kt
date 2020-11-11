package com.example.cemeterywithserver.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.cemeterywithserver.data.entitites.Cemetery
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CemeteryDaoTest {

    private lateinit var database: CemeteryDatabase
    private lateinit var dao: CemeteryDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CemeteryDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.cemeteryDao()
    }

    @After
    fun tearDown() {
        database.close()

    }

    @Test
    fun check_getUnsyncedCemeteries_should_return_list()= runBlockingTest {
        val cemeteryList = listOf<Cemetery>(
            Cemetery(
                cemeteryRowId = "djdjdjdj",
                cemeteryLocation = "Thorsby",
                cemeteryState = "Al",
                cemeteryCounty = "Chilton",
                cemeteryName = "Thorsby cemetery",
                section = "Thorsby section",
                firstYear = "1234",
                range = "Thorsby range",
                spot = "Spot",
                township = "township"
            ),
            Cemetery(
                cemeteryRowId = "uryryrytu",
                cemeteryLocation = "Thorsby",
                cemeteryState = "Al",
                cemeteryCounty = "Chilton",
                cemeteryName = "Thorsby cemetery",
                section = "Thorsby section",
                firstYear = "1234",
                range = "Thorsby range",
                spot = "Spot",
                township = "township"
            )
        )

        dao.insertAllCemsFromNetwork(cemeteryList)
        val unsyncedCems = dao.getUnSynchedCemeteries()

        assertThat(unsyncedCems.size).isEqualTo(2)

    }


}
