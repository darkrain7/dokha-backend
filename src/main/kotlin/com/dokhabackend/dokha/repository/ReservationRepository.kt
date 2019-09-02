package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.Reservation
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

interface ReservationRepository : CrudRepository<Reservation, Long> {

    fun findByPlaceReservationId(placeId: Long): Collection<Reservation>

    @Query("SELECT *" +
            " FROM dokha.reservation r" +
            " INNER JOIN dokha.timetable t ON t.id = r.timetable_id" +
            " WHERE t.working_date = :workingDate" +
            " AND r.place_id = :placeId", nativeQuery = true)
    fun findByPlaceIdAndDate(@Param("placeId") placeId: Long, @Param("workingDate") workingDate: Long): Collection<Reservation>

    @Query(value = "SELECT *" +
            " FROM dokha.reservation r" +
            " WHERE r.reservation_start_time >= :startTime " +
            " AND r.reservation_end_time <= :endTime" +
            " AND place_id = :placeId ", nativeQuery = true)
    fun findByPlaceIdAndDateInterval(@Param("placeId") placeId: Long,
                                     @Param("startTime") startTime: LocalDateTime,
                                     @Param("endTime") endTime: LocalDateTime): Collection<Reservation>
}