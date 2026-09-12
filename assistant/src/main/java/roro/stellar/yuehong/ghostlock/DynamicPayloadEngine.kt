package roro.stellar.yuehong.ghostlock

import com.kernelpack.KernelPack
import com.kernelpack.PackRequest
import com.kernelpack.profile.BaselineProfiles
import java.util.function.Consumer

/** Small Java-facing bridge around the pure Kotlin KSuRoot kernel packer. */
data class DynamicBuildResult(
    val payload: ByteArray?,
    val summary: String,
    val warnings: String,
)

object DynamicPayloadEngine {
    @JvmStatic
    fun build(boot: ByteArray, base: ByteArray, baselineId: String, logger: Consumer<String>): DynamicBuildResult {
        val result = KernelPack.pack(
            PackRequest(
                bootImage = boot,
                baseLibrary = base,
                baseline = BaselineProfiles.byId(baselineId),
                log = { line -> logger.accept(line) },
            ),
        )
        return DynamicBuildResult(
            result.packedLibrary,
            result.summary(),
            result.warnings.joinToString("\n"),
        )
    }
}
