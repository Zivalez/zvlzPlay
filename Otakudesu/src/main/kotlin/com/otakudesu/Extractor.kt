package com.otakudesu

import com.lagradost.cloudstream3.extractors.JWPlayer
import com.lagradost.cloudstream3.extractors.Filesim

class Moedesu : JWPlayer() {
    override val name = "Moedesu"
    override val mainUrl = "https://desustream.me/moedesu/"
}

class DesuBeta : JWPlayer() {
    override val name = "DesuBeta"
    override val mainUrl = "https://desustream.me/beta/"
}

class Desudesuhd : JWPlayer() {
    override val name = "Desudesuhd"
    override val mainUrl = "https://desustream.me/desudesuhd/"
}

class Odvidhide : Filesim() {
    override val name = "Odvidhide"
    override var mainUrl = "https://odvidhide.com"
}
