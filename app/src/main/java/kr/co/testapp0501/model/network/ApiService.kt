package kr.co.testapp0501.model.network

import io.reactivex.Single
import kr.co.testapp0501.model.group.Group
import kr.co.testapp0501.model.group.Info
import kr.co.testapp0501.model.user.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.PartMap as PartMap

interface ApiService {

    companion object{
        const val BASE_URL = "http://192.168.2.55:9999"
        const val PREFIX_URL = "/com/avad/api"

        // 일반 회원가입,로그인
        const val NORMAL_SIGN_UP = "$PREFIX_URL/member/self"
        const val NORMAL_SIGN_IN = "$PREFIX_URL/member/self/login"

        // 소셜 회원가입, 로그인
        const val SNS_SIGN_UP = "$PREFIX_URL/member/sns"
        const val SNS_SIGN_IN = "$PREFIX_URL/member/sns/login"

        // 그룹 생성
        const val GROUP_CREATE = "$PREFIX_URL/group"
        // 그룹 목록
        const val GROUP_LIST = "$PREFIX_URL/group/types"
    }

    // 일반 회원가입 ID 중복체크
    @GET("$PREFIX_URL/member/check/{memberId}")
    fun checkId(@Path("memberId") memberId : CheckId): Call<UserResponse>

    /** ===================== 일반 회원가입,로그인 ======================== */

    // 일반 회원가입
    @Headers("Content-Type: application/json")
    @POST(NORMAL_SIGN_UP)
    fun addNormalUser(@Body normaluser: NormalUser): Call<UserResponse>

    // 일반 로그인
    @Headers("Content-Type: application/json")
    @POST(NORMAL_SIGN_IN)
    fun normalLogin(@Body user: NormalLogin): Call<UserResponse>

    /** ===================== 소셜 회원가입,로그인 =======================*/

    // 소셜 회원가입
    @POST(SNS_SIGN_UP)
    fun addSnsUser(@Body user: SocialUser): Call<UserResponse>

    // 소셜 로그인
    @POST(SNS_SIGN_IN)
    fun snsLogin(@Query("snsId") snsId: String?): Call<UserResponse>

    /** ===================== 그룹 생성,접속 ========================= */

    // 그룹 생성1
    @Headers("Content-Type: application/json")
    @Multipart
    @POST(GROUP_CREATE)
    fun uploadData1(
        @Header("X-AUTH-TOKEN") token: String,
        @PartMap dataPart: Map<String, String>,
        @Part imageFile: MultipartBody.Part
    ): Call<String>

    // 그룹 생성2
    @Headers("Content-Type: application/json")
    @Multipart
    @POST(GROUP_CREATE)
    fun uploadData2(
        @Header("X-AUTH-TOKEN") token: String,
        @Body info: RequestBody,
        @Part imageFile: MultipartBody.Part
    ): Call<String>

    // 그룹 생성3
    @Headers("Content-Type: application/json")
    @POST(GROUP_CREATE)
    fun uploadData3(
        @Header("X-AUTH-TOKEN") token: String,
        @Part("info") info: RequestBody,
        @Part files: List<MultipartBody.Part>
        ): Call<String>

    // 내가 속한 그룹 목록 불러오기
//    @GET(GROUP_LIST)
//    suspend fun loadGroupList(@Query("") group: String): Response<List<String>>
}


























