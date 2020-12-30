/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.tree

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpaceTest {
    @Test
    fun singleLineComment() {
        val cf = Space.format("""
            // I'm a little // teapot
            // Short and stout //
                // Here is my handle
            \u00A0  \u00A0
        """.trimIndent())

        assertThat(cf.comments).hasSize(3)

        val (c1, c2, c3) = cf.comments

        assertThat(c1.text).isEqualTo(" I'm a little // teapot")
        assertThat(c2.text).isEqualTo(" Short and stout //")
        assertThat(c3.text).isEqualTo(" Here is my handle")

        assertThat(c1.suffix).isEqualTo("\n")
        assertThat(c2.suffix).isEqualTo("\n    ")
        assertThat(c3.suffix).isEqualTo("\n\\u00A0  \\u00A0")

        assertThat(cf.whitespace).isEmpty()
    }

    @Test
    fun multiLineComment() {
        val cf = Space.format("""
            /*   /*    Here is my spout     */
            /* When I get all steamed up */
            /* /*
            Here me shout
            */
            \u00A0  \u00A0
        """.trimIndent())

        assertThat(cf.comments).hasSize(3)

        val (c1, c2, c3) = cf.comments

        assertThat(c1.text).isEqualTo("   /*    Here is my spout     ")
        assertThat(c2.text).isEqualTo(" When I get all steamed up ")
        assertThat(c3.text).isEqualTo(" /*\nHere me shout\n")

        assertThat(c1.suffix).isEqualTo("\n")
        assertThat(c2.suffix).isEqualTo("\n    ")
        assertThat(c3.suffix).isEqualTo("\n\\u00A0  \\u00A0")

        assertThat(cf.whitespace).isEmpty()
    }

    @Test
    fun javadocComment() {
        val cf = Space.format("""
            /**
             * /** Tip me over and pour me out!
             */
            \u00A0  \u00A0
        """.trimIndent())

        assertThat(cf.comments).hasSize(1)
        assertThat(cf.comments.first().text).isEqualTo("\n * /** Tip me over and pour me out!\n ")
        assertThat(cf.comments.first().suffix).isEqualTo("""
            
            \u00A0  \u00A0""".trimIndent())
    }
}
