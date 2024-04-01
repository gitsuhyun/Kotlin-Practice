package com.example.todo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao    // DAO 객체는 @DAO를 추가한 interface로 작성
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY date DESC")     //데이터를 가져올 때는 @Query 추가, data를 최근 날짜가 위로 오도록 내림차순으로 정렬하는 쿼리 사용
    fun getAll(): Flow<List<Todo>>                      //반환 타입 Flow, 데이터를 관찰할 수 있도록 해주는 기능

    //데이터를 얻는 동작 이외의 추가, 수정, 삭제는 모두 비동기로 오래 걸림
    //메서드 앞에 suspend 키워드 추가함 -> suspend를 추가하면 오래 걸리는 코드임을 나타내고 코틀린의 비동기 처리 방법인 코루틴을 활용해서 다루어야함
    @Insert(onConflict = OnConflictStrategy.REPLACE)    //추가 , 기본키가 동일한 경우 덮어 쓰는 옵션 추가
    suspend fun insert(entity: Todo)

    @Update                                             //수정
    suspend fun update(entity: Todo)

    @Delete                                             //삭제
    suspend fun delete(entity: Todo)

}