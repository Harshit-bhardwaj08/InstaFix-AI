import retrofit2.http.Body
import retrofit2.http.POST

// ApiService.kt
interface ApiService {
    @POST("diagnosis")
    suspend fun getDiagnosis(@Body request: DiagnosisRequest): DiagnosisResponse
}

data class DiagnosisRequest(val classId: Int, val confidence: String)

data class DiagnosisResponse(
    val diagnosis: String,
    val confidence: String,
    val repairCost: Int,
    val handymanFee: Int,
    val totalCost: Int,
    val videoLink: String
)
