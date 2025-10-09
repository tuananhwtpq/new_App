package com.example.myapp.ui.components


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
//import com.wolt.blurhash.BlurHashDecoder
import androidx.compose.ui.graphics.asImageBitmap
//import com.wolt.blurhash.BlurHashDecoder.decode

//import com.wolt.blurhash.BlurHashDecoder


//@Composable
//fun rememberBlurHashPainter(
//    blurHash: String?,
//    width: Int = 4,
//    height: Int = 3,
//    punch: Float = 1f,
//    fallback: Painter = ColorPainter(Color.LightGray) // Màu dự phòng nếu blurhash lỗi
//): Painter {
//    // Dùng remember để việc giải mã chỉ xảy ra một lần cho mỗi blurHash
//    return remember(blurHash) {
//        if (blurHash.isNullOrBlank()) {
//            fallback
//        } else {
//            // Thực hiện giải mã
//            val bitmap = BlurHashDecoder.decode(blurHash, width, height, punch)
//            // Chuyển Bitmap thành Painter mà Compose có thể vẽ
//            bitmap?.asImageBitmapPainter() ?: fallback
//        }
//    }
//}
//
//// Thêm hàm extension này vào cùng file
//private fun android.graphics.Bitmap.asImageBitmapPainter(): Painter {
//    return androidx.compose.ui.graphics.painter.BitmapPainter(
//        image = this.asImageBitmap()
//    )
//}