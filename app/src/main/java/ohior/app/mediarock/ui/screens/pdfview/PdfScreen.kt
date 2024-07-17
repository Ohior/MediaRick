package ohior.app.mediarock.ui.screens.pdfview

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.dp
import ohior.app.mediarock.R
import ohior.app.mediarock.ui.compose_utils.DisplayLottieAnimation
import ohior.app.mediarock.ui.theme.primaryFontFamily
import ohior.app.mediarock.whenNotNull
import ohior.app.mediarock.whenNull

private class PdfViewScreenImpl {
    @Composable
    fun PdfLottieAnimation(modifier: Modifier = Modifier, onClick: () -> Unit) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DisplayLottieAnimation(
                modifier = Modifier
                    .size(LocalView.current.width.dp()),
                resId = R.raw.pdf_lottie
            )
            ElevatedButton(
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        blue = 0.1f,
                        red = 0.1f,
                        green = 0.1f
                    ),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ), onClick = onClick
            ) {
                Text(
                    text = "select a pdf file.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = primaryFontFamily
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private val pdfViewScreenImpl = PdfViewScreenImpl()


@Composable
fun PdfViewScreen(viewModel: PdfScreenLogic) {
    val pickPdf =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { content ->
            content.whenNull {
                viewModel.isPdfSelected = false
            }.whenNotNull {
                viewModel.isPdfSelected = true
                viewModel.pdfUri = it
            }
        }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        if (!viewModel.isPdfSelected) {
            pdfViewScreenImpl.PdfLottieAnimation(modifier = Modifier.fillMaxSize()) {
                pickPdf.launch("application/pdf")
            }
        } else {
            VerticalPDFReader(
                state = viewModel.getPdfVerticalState(),
                modifier = Modifier.fillMaxSize()
            )
//            RotateScreenButton()
        }
    }
}