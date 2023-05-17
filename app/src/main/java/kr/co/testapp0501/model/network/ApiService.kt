package kr.co.testapp0501.model.network

import kr.co.testapp0501.model.group.*
import kr.co.testapp0501.model.user.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.PartMap as PartMap

interface ApiService {

    companion object{
        const val BASE_URL_FIRST = "http://192.168.2.55:9999"
        const val BASE_URL_SECOND = "http://192.168.2.77:9999"

        private const val PREFIX_URL = "/com/avad/api"
        const val FILE_SUFFIX_URL = "$BASE_URL_FIRST/attachFile"

        // 일반 회원가입 ID 중복체크
        const val NORMAL_ID_CHECK = "$PREFIX_URL/member/check/{memberId}"

        // 일반 회원가입,로그인
        const val NORMAL_SIGN_UP = "$PREFIX_URL/member/self"
        const val NORMAL_SIGN_IN = "$PREFIX_URL/member/self/login"

        // 소셜 회원가입, 로그인
        const val SNS_SIGN_UP = "$PREFIX_URL/member/sns"
        const val SNS_SIGN_IN = "$PREFIX_URL/member/sns/login"

        // 그룹 생성
        const val GROUP_CREATE = "$PREFIX_URL/group"
        // 그룹 매칭
        const val GROUP_MATCHING = "$PREFIX_URL/group/matching"
        // 그룹 형식 리스트
        //const val GROUP_LIST = "$PREFIX_URL/group/types"
        // 회원 그룹 조회
        const val GROUP_LOAD = "$PREFIX_URL/member/{memberSeq}/groups"
        const val GROUP_FILE = "/attachFile{fileUrl}"
        // 그룹 매칭 대기 회원 조회
        const val GROUP_MATCHING_WAITING_LIST = "$PREFIX_URL/group/{groupSeq}/matching/members"
        // 그룹 회원 조회
        const val GROUP_MEMBER_LIST = "$PREFIX_URL/group/{groupSeq}/members"
        // 그룹 매칭 대기 회원 수락
        const val GROUP_MATCHING_WAITING_ACCEPT = "$PREFIX_URL/group/member"
        // 게시글 작성
        const val ALBUM_POST_UPLOAD = "$PREFIX_URL/board"
    }

    // 일반 회원가입 ID 중복체크
    @POST(NORMAL_ID_CHECK)
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

    /** ===================== 그룹 생성,매칭 ========================= */

    // 그룹 생성
    @Multipart
    @POST(GROUP_CREATE)
    fun uploadData(
        @Header("X-AUTH-TOKEN") token: String,
        @Part("info") info: RequestBody,
        @Part imageFile: MultipartBody.Part
    ): Call<String>

    // 그룹 매칭
    @Headers("Content-Type: application/json")
    @POST(GROUP_MATCHING)
    fun groupMatching(
        @Header("X-AUTH-TOKEN") token: String,
        @Body groupMatching: GroupMatching
    ): Call<String>

    // 그룹 형식 리스트
//    @GET(GROUP_LIST)
//    suspend fun loadGroupList(@Query("") group: String): Response<List<String>>

    /** ===================== 그룹 회원,매칭 조회 ========================= */

    // 회원 그룹 조회 [ 회원 그룹목록 불러오기 ]
    @GET(GROUP_LOAD)
    fun loadGroupList(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("memberSeq") memberSeq: Int
    ): Call<GroupList>

    // 그룹 매칭 대기 회원 조회
    @GET(GROUP_MATCHING_WAITING_LIST)
    fun groupMatchingList(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("groupSeq") groupSeq: Int
    ): Call<MatchingWaitingList>

    // 그룹 회원 조회
    @GET(GROUP_MEMBER_LIST)
    fun groupMemberList(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("groupSeq") groupSeq: Int
    ): Call<GroupMemberList>

    // 그룹 매칭 수락
    @POST(GROUP_MATCHING_WAITING_ACCEPT)
    fun groupMatchingAccept(
        @Header("X-AUTH-TOKEN") token: String,
        @Body memberSeq: GroupMatchingAccept
    ): Call<String>

    // 게시글 작성
    @Multipart
    @POST(ALBUM_POST_UPLOAD)
    fun albumPostUpload(
        @Header("X-AUTH-TOKEN") token: String,
        @Part("board") board: RequestBody,
        @Part imageFile: MutableList<MultipartBody.Part>
    ): Call<String>

}


























