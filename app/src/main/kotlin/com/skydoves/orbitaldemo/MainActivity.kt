/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.orbitaldemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.animateMovement
import com.skydoves.orbital.animateSharedElementTransition
import com.skydoves.orbital.animateTransformation
import com.skydoves.orbital.rememberContentWithOrbitalScope
import com.skydoves.orbitaldemo.ui.OrbitalTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      OrbitalTheme {
//      OrbitalSharedElementTransitionExample()
//      OrbitalMultipleSharedElementTransitionExample()
//       OrbitalTransformationExample()
//         OrbitalMovementExample()
        MorphingExample()
      }
    }
  }
}

@Composable
private fun OrbitalTransformationExample() {
  var isTransformed by rememberSaveable { mutableStateOf(false) }
  val poster = rememberContentWithOrbitalScope {
    GlideImage(
      modifier = if (isTransformed) {
        Modifier.size(300.dp, 620.dp)
      } else {
        Modifier.size(100.dp, 2000.dp)
      }.animateTransformation(this, transformationSpec),
      imageModel = { MockUtils.getMockPoster().poster },
      imageOptions = ImageOptions(contentScale = ContentScale.Fit)
    )
  }

  Orbital(
    modifier = Modifier
      .clickable { isTransformed = !isTransformed }
  ) {
    Column(
      Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      poster()
    }
  }
}

@Composable
private fun OrbitalMovementExample() {
  var isTransformed by rememberSaveable { mutableStateOf(false) }
  val poster = rememberContentWithOrbitalScope {
    GlideImage(
      modifier = if (isTransformed) {
        Modifier.size(130.dp, 2000.dp)
      } else {
        Modifier.size(130.dp, 2000.dp)
      }.animateMovement(this, movementSpec),
      imageModel = { ItemUtils.urls[3] },
      imageOptions = ImageOptions(contentScale = ContentScale.Fit)
    )
  }

  Orbital(
    modifier = Modifier
      .clickable { isTransformed = !isTransformed }
  ) {
    if (isTransformed) {
      Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        poster()
      }
    } else {
      Column(
        Modifier
          .fillMaxSize()
          .padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
      ) {
        poster()
      }
    }
  }
}

@Composable
private fun OrbitalSharedElementTransitionExample() {
  var isTransformed by rememberSaveable { mutableStateOf(false) }
  val item = MockUtils.getMockPosters()[3]
  val poster = rememberContentWithOrbitalScope {
    GlideImage(
      modifier = if (isTransformed) {
        Modifier.fillMaxSize()
      } else {
        Modifier.size(130.dp, 2000.dp)
      }.animateSharedElementTransition(
        this,
        SpringSpec(stiffness = 500f),
        SpringSpec(stiffness = 500f)
      ),
      imageModel = { item.poster },
      imageOptions = ImageOptions(contentScale = ContentScale.Fit)
    )
  }

  Orbital(
    modifier = Modifier
      .clickable { isTransformed = !isTransformed }
  ) {
    if (isTransformed) {
      PosterDetails(
        poster = item,
        sharedElementContent = { poster() },
        pressOnBack = {}
      )
    } else {
      Column(
        Modifier
          .fillMaxSize()
          .padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
      ) {
        poster()
      }
    }
  }
}

@Composable
private fun OrbitalMultipleSharedElementTransitionExample() {
  var isTransformed by rememberSaveable { mutableStateOf(false) }
  val items = rememberContentWithOrbitalScope {
    MockUtils.getMockPosters().take(4).forEach { item ->
      GlideImage(
        modifier = if (isTransformed) {
          Modifier.size(140.dp, 180.dp)
        } else {
          Modifier.size(100.dp, 2000.dp)
        }
          .animateSharedElementTransition(this, movementSpec, transformationSpec)
          .padding(8.dp),
        imageModel = { item.poster },
        imageOptions = ImageOptions(contentScale = ContentScale.Fit)
      )
    }
  }

  Orbital(
    modifier = Modifier
      .fillMaxSize()
      .clickable { isTransformed = !isTransformed },
    isTransformed = isTransformed,
    onStartContent = {
      Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        items()
      }
    },
    onTransformedContent = {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) { items() }
    }
  )
}

@Composable
private fun MorphingExample() {
  var state by rememberSaveable { mutableStateOf(0) }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Gray)
      .padding(16.dp)
  ) {
      Surface(
        modifier = Modifier
          .width(100.dp)
          .height(100.dp)
          .align(
            when (state) {
              0 -> Alignment.TopStart
              1 -> Alignment.TopCenter
              2 -> Alignment.TopEnd
              3 -> Alignment.CenterEnd
              4 -> Alignment.BottomEnd
              5 -> Alignment.BottomCenter
              6 -> Alignment.BottomStart
              else -> Alignment.CenterStart
            }
          ),
        color = when (state) {
          0 -> Color.Yellow
          1 -> Color.Magenta
          2 -> Color.Green
          3 -> Color.Cyan
          4 -> Color.Red
          5 -> Color.Black
          6 -> Color.Blue
          else -> Color.White
        },
        shape = when (state) {
          0, 4 -> RectangleShape
          1, 5 -> CircleShape
          2, 6 -> RoundedCornerShape(16.dp)
          else -> CutCornerShape(32.dp)
        }
      ) {
    }
    Button(onClick = { state = (state + 1) % 8 }, modifier = Modifier.align(Alignment.Center)) {
      Text("Toggle", color = Color.White)
    }
  }
}
