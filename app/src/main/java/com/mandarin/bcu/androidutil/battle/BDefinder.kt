package com.mandarin.bcu.androidutil.battle

import android.content.Context
import com.mandarin.bcu.androidutil.StaticStore
import com.mandarin.bcu.androidutil.battle.sound.SoundHandler.read
import common.util.Res
import common.util.pack.Background
import common.util.pack.EffAnim
import common.util.pack.NyCastle
import common.util.pack.Soul

class BDefinder {
    fun define(c: Context) {
        if (!StaticStore.effread) {
            EffAnim.read()
            StaticStore.effread = true
        }
        if (StaticStore.bgread == 0) {
            Background.read()
            StaticStore.bgread = 1
        }
        if (!StaticStore.soulread) {
            Soul.read()
            StaticStore.soulread = true
        }
        if (!StaticStore.nycread) {
            NyCastle.read()
            StaticStore.nycread = true
        }
        if (!StaticStore.resread) {
            Res.readData()
            StaticStore.resread = true
        }
        if (!StaticStore.musicread) {
            read(c)
            StaticStore.musicread = true
        }
    }
}