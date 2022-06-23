package tk.zwander.lockscreenwidgets.compose.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tk.zwander.lockscreenwidgets.compose.main.SubduedOutlinedButton

@Composable
fun ClickableCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String?,
    ) {
    Row(modifier = modifier) {
        SubduedOutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .animateContentSize()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h5
                )

                if (!summary.isNullOrBlank()) {
                    Spacer(Modifier.size(4.dp))

                    Text(
                        text = summary,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}