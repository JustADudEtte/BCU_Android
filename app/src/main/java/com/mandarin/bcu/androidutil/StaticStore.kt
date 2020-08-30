package com.mandarin.bcu.androidutil

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonParser
import com.mandarin.bcu.R
import com.mandarin.bcu.androidutil.StatFilterElement.Companion.statFilter
import com.mandarin.bcu.androidutil.io.AContext
import com.mandarin.bcu.androidutil.io.ErrorLogWriter.Companion.writeDriveLog
import common.CommonStatic
import common.battle.BasisLU
import common.battle.BasisSet
import common.io.json.JsonDecoder
import common.pack.Identifier
import common.pack.IndexContainer.Indexable
import common.pack.PackData
import common.pack.UserProfile
import common.system.fake.FakeImage
import common.system.files.VFile
import common.util.Data
import common.util.anim.AnimU.UType
import common.util.anim.ImgCut
import common.util.lang.MultiLangCont
import common.util.stage.Music
import common.util.unit.Combo
import common.util.unit.Enemy
import common.util.unit.Form
import common.util.unit.Unit
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.ln

object StaticStore {
    //System & IO variables
    /**Version of Application */
    const val VER = "0.15.0"

    /**Fild ID of google drive log folder */
    const val ERR_FILE_ID = "1F60YLwsJ_zrJOh0IczUuf-Q1QyJftWzK"

    /**Required libraries list */
    val LIBREQ = arrayOf("000001", "000002", "000003", "000004", "000005", "000006", "000007", "000008", "000009", "000010", "090900", "090901")

    /**Locale codes list */
    val lang = arrayOf("", "en", "zh", "ko", "ja", "ru", "de", "fr", "nl", "es")

    /**List of language files */
    val langfile = arrayOf("EnemyName.txt", "StageName.txt", "UnitName.txt", "UnitExplanation.txt", "EnemyExplanation.txt", "CatFruitExplanation.txt", "RewardName.txt", "ComboName.txt", "MedalName.txt", "MedalExplanation.txt")

    /**Shared preferences name */
    const val CONFIG = "configuration"

    /**
     * This value prevents button is performed less than every 1 sec<br></br>
     * Used when preventing activity is opened double
     */
    const val INTERVAL: Long = 1000

    /**
     * This value prevents button is performed less than every 350 ms<br></br>
     * Used when preventing animation working incorrectly
     */
    const val INFO_INTERVAL: Long = 350

    /** Value which tells if Background data is loaded  */
    var bgread = 0

    /** Value which tells if Unit language data is loaded  */
    var unitlang = 1

    /** Value which tells if Enemy language data is loaded  */
    var enemeylang = 1

    /** Value which tells if Stage language data is loaded  */
    var stagelang = 1

    /** Value which tells if Map language data is loaded  */
    var maplang = 1

    /** Value which tells if Medal language data is loaded  */
    var medallang = 1

    /** Boolean which tells if error log dialog is already opened once  */
    var dialogisShowed = false

    /** Boolean which tells if user allowed auto error log uploading  */
    var upload = false

    /**
     * Toast which is used in every activity<br></br>
     * Must be null when activity is destroyed to prevent memory leaks
     */
    var toast: Toast? = null

    /** Value which tells if file paths are added to memory  */
    var init = false

    /** Initial vector used when encrypt/decrypt images  */
    @JvmField
    var IV = "1234567812345678"
    //Image/Text variable

    /** img15.png's parts  */
    var img15: Array<FakeImage>? = null

    /** Ability icons  */
    var icons: Array<Bitmap>? = null

    /** Proc icons  */
    var picons: Array<Bitmap>? = null

    /** Cat fruit icons  */
    var fruit: Array<Bitmap>? = null

    /** Additional ability explanation texts  */
    var addition: Array<String> = Array(0) {""}

    /** Imgcut index list of ablities  */
    var anumber = intArrayOf(203, 204, 206, 202, 205, 200, 209, 227, 218, 227, 227, 227, 227, 260, 258, 227, 227, 110, 227, 227, 122, 114)

    /** Imgcut index list of procs  */
    var pnumber = intArrayOf(207, 197, 198, 201, 208, 195, 264, 266, 289, 196, 199, 227, 227, 216, 214, 215, 210, 213, 262, 116, 227, 227, 227, 227, 227, 227, 227, 227, 227, 229, 231, 227, 239, 237, 243, 227, 227, 49, 45, 47, 51, 43, 53, 109)

    /** File index list of abilities  */
    var afiles = arrayOf("", "", "", "", "", "", "", "MovingX.png", "", "SnipeX.png", "TimeX.png", "Ghost.png", "PoisonX.png", "", "", "Suicide.png", "ThemeX.png",
            "", "SealX.png", "BossWaveX.png", "", "")

    /** File index list of procs  */
    var pfiles = arrayOf("", "", "", "", "", "", "", "", "", "", "", "Burrow.png", "Revive.png", "", "", "", "", "", "", "", "Snipe.png", "Time.png", "Seal.png", "Summon.png", "Moving.png", "Theme.png", "Poison.png", "BossWave.png", "CritX.png", "", "", "BCPoison.png", "", "", "", "ArmorBreak.png", "Speed.png")

    /** String ID list of traits  */
    var colorid = intArrayOf(R.string.sch_wh, R.string.sch_red, R.string.sch_fl, R.string.sch_bla, R.string.sch_me, R.string.sch_an, R.string.sch_al, R.string.sch_zo, R.string.sch_re, R.string.esch_eva, R.string.esch_witch)

    /** String ID list of star and mask treasure  */
    var starid = intArrayOf(R.string.unit_info_starred, R.string.unit_info_god1, R.string.unit_info_god2, R.string.unit_info_god3)

    /** String ID list of procs  */
    var procid = intArrayOf(R.string.sch_abi_kb, R.string.sch_abi_fr, R.string.sch_abi_sl, R.string.sch_abi_cr, R.string.sch_abi_wv, R.string.sch_abi_we, R.string.sch_abi_bb, R.string.sch_abi_wa, R.string.sch_abi_cu,
            R.string.sch_abi_str, R.string.sch_abi_su, R.string.abi_bu, R.string.abi_rev, R.string.sch_abi_ik, R.string.sch_abi_if, R.string.sch_abi_is, R.string.sch_abi_iwv, R.string.sch_abi_iw, R.string.sch_abi_iwa,
            R.string.sch_abi_ic, R.string.abi_snk, R.string.abi_stt, R.string.abi_seal, R.string.abi_sum, R.string.abi_mvatk, R.string.abi_thch, R.string.abi_poi, R.string.abi_boswv, R.string.abi_imcri, R.string.sch_abi_sb, R.string.sch_abi_iv, R.string.sch_abi_poi, R.string.sch_abi_surge, R.string.sch_abi_impoi, R.string.sch_abi_imsu, R.string.abi_armbr, R.string.abi_hast, R.string.talen_kb, R.string.talen_fr,
            R.string.talen_sl, R.string.talen_wv, R.string.talen_we, R.string.talen_warp, R.string.talen_cu, R.string.talen_poi, R.string.talen_sur)

    /** String ID list of abilities  */
    var abiid = intArrayOf(R.string.sch_abi_st, R.string.sch_abi_re, R.string.sch_abi_md, R.string.sch_abi_ao, R.string.sch_abi_em, R.string.sch_abi_bd, R.string.sch_abi_me, R.string.abi_imvatk, R.string.sch_abi_ws,
            R.string.abi_isnk, R.string.abi_istt, R.string.abi_gh, R.string.abi_ipoi, R.string.sch_abi_zk, R.string.sch_abi_wk, R.string.abi_sui, R.string.abi_ithch, R.string.sch_abi_eva,
            R.string.abi_iseal, R.string.abi_iboswv, R.string.sch_abi_it, R.string.sch_abi_id)

    /** String ID list of additional explanations  */
    var textid = intArrayOf(R.string.unit_info_text0, R.string.unit_info_text1, R.string.unit_info_text2, R.string.unit_info_text3, R.string.unit_info_text4, R.string.unit_info_text5, R.string.unit_info_text6, R.string.unit_info_text7,
            R.string.def_unit_info_text8, R.string.unit_info_text9, R.string.unit_info_text10, R.string.def_unit_info_text11, R.string.def_unit_info_text12, R.string.unit_info_text13,
            R.string.unit_info_text14, R.string.unit_info_text15, R.string.unit_info_text16, R.string.unit_info_text17, R.string.unit_info_text18, R.string.unit_info_text19, R.string.unit_info_text20,
            R.string.unit_info_text21, R.string.unit_info_text22, R.string.unit_info_text23, R.string.unit_info_text24, R.string.unit_info_text25, R.string.unit_info_text26,
            R.string.unit_info_text27, R.string.unit_info_text28, R.string.unit_info_text29, R.string.unit_info_text30, R.string.unit_info_text31, R.string.unit_info_text32, R.string.unit_info_text33, R.string.unit_info_text34, R.string.unit_info_text35, R.string.unit_info_text36, R.string.unit_info_text37, R.string.unit_info_text38, R.string.unit_info_text39, R.string.unit_info_text40, R.string.unit_info_text41, R.string.unit_info_text42, R.string.unit_info_text43, R.string.unit_info_text44, R.string.unit_info_text45, R.string.unit_info_text46, R.string.unit_info_text47, R.string.unit_info_text48, R.string.unit_info_text49, R.string.unit_info_text50)
    //Variables for Unit

    private var unitinflistClick = SystemClock.elapsedRealtime()
    var UisOpen = false
    var unittabposition = 0
    var unitinfreset = false

    /**
     * Variables for Enemy
     */
    var enemyinflistClick = SystemClock.elapsedRealtime()
    var EisOpen = false

    /**
     * Variables for Map/Stage
     */
    var bcMapNames = intArrayOf(R.string.stage_sol, R.string.stage_event, R.string.stage_collabo, R.string.stage_eoc, R.string.stage_ex, R.string.stage_dojo, R.string.stage_heavenly, R.string.stage_ranking, R.string.stage_challenge, R.string.stage_uncanny, R.string.stage_night, R.string.stage_baron, R.string.stage_enigma, R.string.stage_CA)
    var mapcolcname: ArrayList<String> = ArrayList()
    var mapcode: ArrayList<String> = ArrayList(listOf("000000", "000001", "000002", "000003", "000004", "000006", "000007", "000011", "000012", "000013", "000014", "000024", "000025", "000027"))
    var BCmaps = mapcode.size
    var eicons: Array<Bitmap>? = null
    var maplistClick = SystemClock.elapsedRealtime()
    var stglistClick = SystemClock.elapsedRealtime()
    var infoClick = SystemClock.elapsedRealtime()
    var treasure: Bitmap? = null
    var infoOpened: BooleanArray? = null
    var stageSpinner = -1
    private var bgnumber = 0
    var bglistClick = SystemClock.elapsedRealtime()
    var stgenem: ArrayList<Enemy> = ArrayList()
    var stgenemorand = true
    var stgmusic = ""
    var stgbg = ""
    var stgstar = 0
    var stgbh = -1
    var bhop = -1
    var stgcontin = -1
    var stgboss = -1
    var filter: Map<String, SparseArray<ArrayList<Int>>>? = null

    /**
     * Variables for Medal
     */
    var medalnumber = 0
    var medals: ArrayList<Bitmap> = ArrayList()
    var MEDNAME = MultiLangCont<Int, String>()
    var MEDEXP = MultiLangCont<Int, String>()

    /**
     * Variables for Music
     */
    var musicnames: MutableMap<String, SparseArray<String>> = HashMap()
    var musicData: MutableList<Identifier<Music>> = ArrayList()
    var durations: MutableList<Int> = ArrayList()

    /**
     * Variables for Animation
     */
    var play = true
    var frame = 0
    var formposition = 0
    var animposition = 0
    var gifFrame = 0
    var gifisSaving = false
    var enableGIF = false
    var keepDoing = true

    /**
     * Variables for LineUp
     */
    var ludata = ArrayList<Identifier<Unit>>()
    var LULoading = false
    var LUread = false
    var LUtabPosition = 0
    var currentForms: ArrayList<Form?> = ArrayList()
    var position = intArrayOf(-1, -1)
    var combos: ArrayList<Combo> = ArrayList()
    var updateList = false
    var updateForm = true
    var updateOrb = true
    var updateTreasure = false
    var updateConst = false
    var updateCastle = false
    var set: BasisSet? = null
    var lu: BasisLU? = null

    /**
     * Search Filter Variables
     */
    var tg = ArrayList<String>()
    var rare = ArrayList<String>()
    var ability = ArrayList<ArrayList<Int>>()
    var attack = ArrayList<String>()
    var tgorand = true
    var atksimu = true
    var aborand = true
    var atkorand = true
    var empty = true
    var talents = false
    var starred = false
    var entityname = ""
    var stgschname = ""
    var stmschname = ""
    var filterEntityList = BooleanArray(1)

    /**
     * Resets all values stored in StaticStore<br></br>
     * It will also reset whole data of BCU
     */
    fun clear() {
        bgread = 0
        unitlang = 1
        enemeylang = 1
        stagelang = 1
        maplang = 1
        medallang = 1
        toast = null
        img15 = null
        icons = null
        picons = null
        fruit = null
        addition = Array(0) {""}
        unitinflistClick = SystemClock.elapsedRealtime()
        UisOpen = false
        unittabposition = 0
        unitinfreset = false
        enemyinflistClick = SystemClock.elapsedRealtime()
        EisOpen = false
        medalnumber = 0
        medals.clear()
        MEDNAME.clear()
        MEDEXP.clear()
        musicnames.clear()
        musicData.clear()
        durations.clear()
        mapcolcname = ArrayList()
        eicons = null
        maplistClick = SystemClock.elapsedRealtime()
        stglistClick = SystemClock.elapsedRealtime()
        infoClick = SystemClock.elapsedRealtime()
        treasure = null
        infoOpened = null
        stageSpinner = -1
        bgnumber = 0
        bglistClick = SystemClock.elapsedRealtime()
        stgenem = ArrayList()
        stgenemorand = true
        stgmusic = ""
        stgbg = ""
        stgstar = 0
        stgbh = -1
        bhop = -1
        stgcontin = -1
        stgboss = -1
        stgschname = ""
        stmschname = ""
        filterEntityList = BooleanArray(1)
        ludata = ArrayList()
        LULoading = false
        LUread = false
        LUtabPosition = 0
        currentForms.clear()
        updateForm = true
        updateOrb = true
        position = intArrayOf(-1, -1)
        combos.clear()
        updateList = false
        set = null
        lu = null
        play = true
        frame = 0
        formposition = 0
        animposition = 0
        gifFrame = 0
        gifisSaving = false
        enableGIF = false
        keepDoing = true

        filterReset()
        stgFilterReset()
    }

    fun getResize(drawable: Drawable, context: Context, dp: Float): Bitmap {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
        val b = (drawable as BitmapDrawable).bitmap
        return Bitmap.createScaledBitmap(b, px.toInt(), px.toInt(), false)
    }

    fun getResizeb(b: Bitmap?, context: Context, dp: Float): Bitmap {
        if (b == null) return empty(context, dp, dp)
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
        val bd = BitmapDrawable(context.resources, Bitmap.createScaledBitmap(b, px.toInt(), px.toInt(), true))
        bd.isFilterBitmap = true
        bd.setAntiAlias(true)
        return bd.bitmap
    }

    /**
     * Gets resized Bitmap.
     *
     * @param b       Source Bitmap.
     * @param context Used when converting dpi value to pixel value.
     * @param w       Width of generated Bitmap. Must be dpi value.
     * @param h       Height of generated Bitmap. Must be dpi value.
     * @return Returns resized Bitmap using specified dpi value.
     */
    fun getResizeb(b: Bitmap?, context: Context, w: Float, h: Float): Bitmap {
        if (b == null) return empty(context, w, h)
        val r = context.resources
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, r.displayMetrics)
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, r.displayMetrics)
        val bd = BitmapDrawable(context.resources, Bitmap.createScaledBitmap(b, width.toInt(), height.toInt(), true))
        bd.isFilterBitmap = true
        bd.setAntiAlias(true)
        return bd.bitmap
    }

    /**
     * Generates empty Bitmap.
     *
     * @param context Used when converting dpi value to pixel value.
     * @param w       Width of generated Bitmap. Must be dpi value.
     * @param h       Height of generated Bitmap. Must be dpi value.
     * @return Returns empty Bitmap using specified dpi value.
     */
    fun empty(context: Context?, w: Float, h: Float): Bitmap {
        context ?: return empty(1, 1)

        val r = context.resources
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, r.displayMetrics)
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, r.displayMetrics)
        val conf = Bitmap.Config.ARGB_8888
        return Bitmap.createBitmap(width.toInt(), height.toInt(), conf)
    }

    /**
     * Generates empty Bitmap.
     *
     * @param w Width of generated Bitmap. Must be pixel value.
     * @param h Height of generated Bitmap. Must be pixel value.
     * @return Returns empty Bitmap using specified pixel value.
     */
    fun empty(w: Int, h: Int): Bitmap {
        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    }

    /**
     * Gets resized bitmap using antialiasing.
     *
     * @param b       Source Bitmap.
     * @param context Used when initializing BitmapDrawable.
     * @param w       Width of generated Bitmap. Must be pixel value.
     * @param h       Height of generated Bitmap. Must be pixel value.
     * @return Returns resized bitmap using antialiasing.
     */
    fun getResizebp(b: Bitmap?, context: Context, w: Float, h: Float): Bitmap {
        val bd = BitmapDrawable(context.resources, Bitmap.createScaledBitmap(b!!, w.toInt(), h.toInt(), true))
        bd.isFilterBitmap = true
        bd.setAntiAlias(true)
        return bd.bitmap
    }

    /**
     * Gets resized bitmap.
     *
     * @param b Source Bitmap.
     * @param w Width of generated Bitmap. Must be pixel value.
     * @param h Height of generated Bitmap. Must be pixel value.
     * @return Returns resized bitmap using specified width and height.
     */
    fun getResizebp(b: Bitmap, w: Float, h: Float): Bitmap {
        val matrix = Matrix()
        if (w < 0 || h < 0) {
            if (w < 0 && h < 0) {
                matrix.setScale(-1f, -1f)
            } else if (w < 0) {
                matrix.setScale(-1f, 1f)
            } else if (h < 0) {
                matrix.setScale(1f, -1f)
            }
        }
        var reversed = Bitmap.createBitmap(b, 0, 0, b.width, b.height, matrix, false)
        reversed = Bitmap.createScaledBitmap(reversed, abs(w).toInt(), abs(h).toInt(), true)
        return reversed
    }

    fun dptopx(dp: Float, context: Context?): Int {
        context ?: return 1

        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
    }

    /**
     * Saves img15 as cut state by img015.imgcut.
     */
    fun readImg() {
        val path = "./org/page/img015.png"
        val imgcut = "./org/page/img015.imgcut"

        val img = ImgCut.newIns(imgcut)

        img15 = try {
            val png = VFile.get(path).data.img
            img.cut(png)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Reads Treasure Radar icon.
     */
    fun readTreasureIcon(c: Context?) {
        val path = getExternalPath(c) + "org/page/img002.png"
        val imgcut = "./org/page/img002.imgcut"
        val f = File(path)
        val img = ImgCut.newIns(imgcut)
        try {
            val png = FakeImage.read(f)
            val imgs = img.cut(png)
            treasure = imgs[28].bimg() as Bitmap
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Decides CommonStatic.lang value.
     *
     * @param lan Code of language refers to StaticStore.lang.<br></br>
     * 0 is Auto.
     */
    fun getLang(lan: Int) {
        val language: String
        if (lan == 0) {
            language = Resources.getSystem().configuration.locales[0].language
            CommonStatic.getConfig().lang = listOf(*lang).indexOf(language) - 1
            println("Auto Set : $language")
        } else {
            println(lang[lan])
            CommonStatic.getConfig().lang = lan - 1
        }
        println(CommonStatic.getConfig().lang)
        if (CommonStatic.getConfig().lang >= 4 || CommonStatic.getConfig().lang < 0) CommonStatic.getConfig().lang = 0
    }

    /**
     * Resets entity filter data<br></br>
     * Must be called when exiting Entity list.
     */
    fun filterReset() {
        tg = ArrayList()
        rare = ArrayList()
        ability = ArrayList()
        attack = ArrayList()
        tgorand = true
        atksimu = true
        aborand = true
        atkorand = true
        empty = true
        talents = false
        starred = false
        entityname = ""
        statFilter.clear()
    }

    /**
     * Resets stage filter data<br></br>
     * Must be called when exiting Map list
     */
    fun stgFilterReset() {
        stgenem = ArrayList()
        stgenemorand = true
        stgmusic = ""
        stgstar = 0
        stgbh = -1
        bhop = -1
        stgcontin = -1
        stgboss = -1
        stgschname = ""
        stmschname = ""
        filter = null
    }

    /**
     * Gets possible position in specific lineup.
     *
     * @param f Arrays of forms in Lineup.
     * @return Returns first empty position in Lineup.
     * If Lineup is full, it will return position of replacing area.
     */
    fun getPossiblePosition(f: Array<Array<Form?>>): IntArray {
        for (i in f.indices) {
            for (j in f[i].indices) {
                if (f[i][j] == null) return intArrayOf(i, j)
            }
        }
        return intArrayOf(100, 100)
    }

    /**
     * Get Color value using Attr ID.
     *
     * @param context     Decides TypedValue using Theme from Context.
     * @param attributeId ID of color from Attr. Format must be color.
     * @return Gets real ID of color considering Theme.
     * It will return Color value as Hex.
     */
    fun getAttributeColor(context: Context?, attributeId: Int): Int {
        context ?: return 0

        val typedValue = TypedValue()
        context.theme.resolveAttribute(attributeId, typedValue, true)
        val colorRes = typedValue.resourceId
        var color = -1
        try {
            color = ContextCompat.getColor(context, colorRes)
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
        return color
    }

    /**
     * Generate Bitmap from Vector Asset.
     * Icon's tint color is ?attr/TextPrimary.
     *
     * @param context Get drawable and set tint color to it.
     * @param vectid  Id from Vector Asset. Use "R.drawable._ID_".
     * @return Returns created Bitmap using Vector Asset.
     * If vectid returns null, then it will generate empty icon.
     */
    fun getBitmapFromVector(context: Context?, vectid: Int): Bitmap {
        context ?: return empty(1,1)

        val drawable = ContextCompat.getDrawable(context, vectid)
                ?: return empty(context, 100f, 100f)
        drawable.setTint(getAttributeColor(context, R.attr.TextPrimary))
        val res = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(res)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return res
    }

    /**
     * Check if specified icon has different width and height.
     * If they are different, it will generate icon which has same width and height.
     * Width and Height's default value is 128 pixels.
     *
     * @param context Using Context to convert dpi value to pixel value.
     * @param b       Source Bitmap.
     * @param wh      This parameter decides width and height of created icon.
     * It must be dpi value.
     * @return If source has same width and height, it will return source.
     * If not, it will return icon which has same width and height.
     */
    fun makeIcon(context: Context, b: Bitmap?, wh: Float): Bitmap {
        if (b == null || b.isRecycled) return empty(context, 24f, 24f)
        if (b.height == b.width) return getResizeb(b, context, wh)
        val before = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
        val c = Canvas(before)
        val p = Paint()
        c.drawBitmap(b, 64 - b.width / 2f, 64 - b.height / 2f, p)
        return getResizeb(before, context, wh)
    }

    /**
     * Check if specified icon has different width and height<br></br>
     * If they are different, it will generate icon which has same width and height<br></br>
     * Width and Height's default value is 128 pixels
     *
     * @param b  Source Bitmap
     * @param wh This parameter decides width and height of created icon<br></br>
     * It must be pixel value
     * @return If source has same width and height, it will return source<br></br>
     * If not, it will return icon which has same width and height
     */
    fun makeIconp(b: Bitmap?, wh: Float): Bitmap {
        if (b == null) return empty(128, 128)
        if (b.height == b.width) return getResizebp(b, wh, wh)
        val before = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
        val c = Canvas(before)
        val p = Paint()
        c.drawBitmap(b, 64 - b.width / 2f, 64 - b.height / 2f, p)
        return if (wh == 128f) before else getResizebp(before, wh, wh)
    }

    /**
     * Saves lineup file.
     */
    @Throws(Exception::class)
    fun saveLineUp(c: Context) {
        val direct = getExternalUser(c)
        val path = "${direct}basis.json"
        val g = File(direct)
        if (!g.exists()) if (!g.mkdirs()) {
            Log.e("SaveLineUp", "Failed to create directory " + g.absolutePath)
        }
        val f = File(path)
        if (!f.exists()) if (!f.createNewFile()) {
            Log.e("SaveLineUp", "Failed to create file " + f.absolutePath)
        }

        BasisSet.write()
    }

    /**
     * Get RGB value from specified HEX color value
     *
     * @param hex Color HEX value which will be converted to RGB values
     * @return Return as three integer array, first is R, second is G, and third is B
     */
    fun getRGB(hex: Int): IntArray {
        val r = hex and 0xFF0000 shr 16
        val g = hex and 0xFF00 shr 8
        val b = hex and 0xFF
        return intArrayOf(r, g, b)
    }

    /**
     * Get scaled volume value considering log calculation
     *
     * @param v This parameter must be 0 ~ 99<br></br>
     * If vol is lower than 0, then it will consider as 0<br></br>
     * If vol is larger than 99, then it will consider as 99<br></br>
     * @return Volume is scaled as logarithmically, it will return calculated value
     */
    fun getVolumScaler(v: Int): Float {
        var vol = v
        if (vol < 0) vol = 0
        if (vol >= 100) vol = 99
        return (1 - ln(100 - vol.toDouble()) / ln(100.0)).toFloat()
    }

    /**
     * Show Toast message using specified String message
     * @param context This parameter is used for Toast.makeText()
     * @param msg String message
     */
    @SuppressLint("ShowToast")
    fun showShortMessage(context: Context?, msg: String?) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }

    /**
     * Show Toast message using specified resource ID
     * @param context Used when Toast.makeText() and getting String from resource ID
     * @param resid Resource ID of String
     */
    @SuppressLint("ShowToast")
    fun showShortMessage(context: Context, resid: Int) {
        val msg = context.getString(resid)
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }

    /**
     * Show Snackbar message using resource ID
     * @param view Targeted view which snackbar will be shown
     * @param resid Resource ID of String
     */
    fun showShortSnack(view: View?, resid: Int) {
        Snackbar.make(view!!, resid, BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    /**
     * Show Snackbar message with specified length using resource ID
     *
     * @param view Targeted view which snackbar will be shown
     * @param resid Resource ID of String
     * @param length Length which snackbar will be shown
     */
    fun showShortSnack(view: View?, resid: Int, length: Int) {
        val snack = Snackbar.make(view!!, resid, length)
        val v = snack.view
        val params = v.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        v.layoutParams = params
        snack.show()
    }

    /**
     * Show Snackbar message with specified String
     *
     * @param view Targeted view which snackbar will be shown
     * @param msg Message as String format
     */
    fun showShortSnack(view: View?, msg: String?) {
        Snackbar.make(view!!, msg!!, BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    /**
     * Show Snackbar message with specified String and length
     *
     * @param view Targeted view which snackbar will be shown
     * @param msg Message as String format
     * @param length Length which snackbar will be shown
     */
    fun showShortSnack(view: View?, msg: String?, length: Int) {
        val snack = Snackbar.make(view!!, msg!!, length)
        val v = snack.view
        val params = v.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        v.layoutParams = params
        snack.show()
    }

    fun getExternalPath(c: Context?): String {
        if (c == null) {
            return ""
        }
        val d = c.getExternalFilesDir(null)
        return if (d != null) {
            d.absolutePath + "/"
        } else {
            ""
        }
    }

    fun getExternalAsset(c: Context?): String {
        return getExternalPath(c) + "assets/"
    }

    @JvmStatic
    fun getExternalPack(c: Context?): String {
        return getExternalPath(c) + "pack/"
    }

    fun getExternalLog(c: Context?): String {
        return getExternalPath(c) + "logs/"
    }

    @JvmStatic
    fun getExternalRes(c: Context?): String {
        return getExternalPath(c) + "res/"
    }

    val dataPath: String
        get() = Environment.getDataDirectory().absolutePath + "/data/com.mandarin.bcu/"

    fun getExternalWorkspace(c: Context?): String {
        return getExternalPath(c) + "workspace/"
    }

    fun getExternalUser(c: Context?): String {
        return getExternalPath(c) + "user/"
    }

    fun checkFolders(vararg pathes: String) {
        for (path in pathes) {
            val f = File(path)
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    Log.e("checkFolders", "Failed to create directory " + f.absolutePath)
                }
            }
        }
    }

    /**
     * Encrypts png file to bcuimg files with specified password and initial vector
     * @param path Path of source file
     * @param key Password
     * @param iv Initial Vector
     * @param delete If this boolean is true, source file will be deleted
     */
    @Throws(IOException::class, NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun encryptPNG(path: String, key: String, iv: String, delete: Boolean) {
        val fis = FileInputStream(path)
        val encPath = path.replace(".png", ".bcuimg")
        val f = File(encPath)
        if (!f.exists()) {
            if (!f.createNewFile()) {
                Log.e("PngEncrypter", "Failed to create file $encPath")
            }
        }
        val fos = FileOutputStream(f)
        val k = key.toByteArray().copyOf(16)
        val v = iv.toByteArray()
        val sks = SecretKeySpec(k, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val parameter = IvParameterSpec(v)
        cipher.init(Cipher.ENCRYPT_MODE, sks, parameter)
        val input = ByteArray(64)
        var i: Int
        while (fis.read(input).also { i = it } != -1) {
            val output = cipher.update(input, 0, i)
            if (output != null) {
                fos.write(output)
            }
        }
        val output = cipher.doFinal()
        if (output != null) fos.write(output)
        if (delete) {
            val g = File(path)
            if (!g.delete()) {
                Log.e("PngEncrypter", "Failed to delete source image $path")
            }
        }
        fis.close()
        fos.close()
    }

    /**
     * Decrypts bcuimg file to png file<br></br>Must be temporary for security
     * @param path Path of encrypted file
     * @param key Password
     * @param iv Initial Vector
     */
    @JvmStatic
    @Throws(IOException::class, NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun decryptPNG(path: String, key: String, iv: String): String {
        val dirs = path.split("/".toRegex()).toTypedArray()
        val name = dirs[dirs.size - 1].replace(".bcuimg", ".png")
        val temp = Environment.getDataDirectory().absolutePath + "/data/com.mandarin.bcu/temp/"
        val g = File(temp)
        if (!g.exists()) {
            if (!g.mkdirs()) {
                Log.e("PngDecrypter", "Failed to create directory $temp")
            }
        }
        val h = File(temp, name)
        if (!h.exists()) {
            if (!h.createNewFile()) {
                Log.e("PngDecrypter", "Failed to create file " + h.absolutePath)
            }
        }
        val fis = FileInputStream(path)
        val fos = FileOutputStream(h)
        val k = key.toByteArray().copyOf(16)
        val v = iv.toByteArray()
        val sks = SecretKeySpec(k, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val parameter = IvParameterSpec(v)
        cipher.init(Cipher.DECRYPT_MODE, sks, parameter)
        val input = ByteArray(64)
        var i: Int
        while (fis.read(input).also { i = it } != -1) {
            val output = cipher.update(input, 0, i)
            if (output != null) {
                fos.write(output)
            }
        }
        val output = cipher.doFinal()
        if (output != null) {
            fos.write(output)
        }
        fis.close()
        fos.close()
        return h.absolutePath
    }

    /**
     * Converts file contents to MD5 code
     *
     * @param f File which will be converted to MD5
     * @return If file doesn't exist it will return empty String<br></br>If there were no problems, it will return converted MD5 code
     */
    @JvmStatic
    @Throws(IOException::class, NoSuchAlgorithmException::class)
    fun fileToMD5(f: File?): String {
        if (f == null || !f.exists()) return ""
        val fis = FileInputStream(f)
        val buffer = ByteArray(1024)
        val md5 = MessageDigest.getInstance("MD5")
        var n: Int
        while (fis.read(buffer).also { n = it } != -1) {
            md5.update(buffer, 0, n)
        }
        val msg = md5.digest()
        val i = BigInteger(1, msg)
        val str = i.toString(16)
        return String.format("%32s", str).replace(' ', '0')
    }

    /** Remove whole files in specified path<br></br>It will delete itself too */
    fun removeAllFiles(f: File) {
        if (f.isFile) {
            if (!f.delete()) {
                Log.e("StaticStore.removeFiles", "Failed to remove file " + f.absolutePath)
            }
        } else if (f.isDirectory) {
            val lit = f.listFiles() ?: return
            for (fs in lit) {
                if (fs.isDirectory) {
                    removeAllFiles(fs)
                    if (!fs.delete()) {
                        Log.e("StaticStore.removeFiles", "Failed to remove directory " + fs.absolutePath)
                    }
                } else if (fs.isFile) {
                    if (!fs.delete()) {
                        Log.e("StaticStore.removeFiles", "Failed to remove file " + fs.absolutePath)
                    }
                }
            }
            if (!f.delete()) {
                Log.e("StaticStore.removeFiles", "Failed to remove direcotry " + f.absolutePath)
            }
        }
    }

    /**
     * Get password from specified shared preference with specified reference
     * @param name Name of shared preferences
     * @param ref Reference of password
     * @param c Context
     * @return It will return password as String if it worked properly
     */
    @JvmStatic
    fun getPassword(name: String?, ref: String?, c: Context): String? {
        val shared = c.getSharedPreferences(name, Context.MODE_PRIVATE)
        return shared.getString(ref, "")
    }

    @JvmStatic
    val isEnglish: Boolean
        get() {
            val lang = Locale.getDefault().language
            return lang != "zh" && lang != "ko" && lang != "ja"
        }

    /**
     * Get current number of pack including [common.pack.PackData.DefPack]
     * @return number of packs
     */
    fun getPackSize(): Int {
        return UserProfile.getAllPacks().size
    }

    /**
     * Generate 3 digit formats
     * @param id ID of specific object
     * @return If id is 1 for example, it will return 001
     */
    fun trio(id: Int): String {
        return when {
            id < 0 -> {
                id.toString()
            }
            id < 10 -> {
                "00$id"
            }
            id < 100 -> {
                "0$id"
            }
            else -> {
                id.toString()
            }
        }
    }

    /**
     * Get list of pack including [common.pack.PackData.DefPack]
     * @return returns list of packs
     */
    fun getPacks(): List<PackData> {
        return ArrayList(UserProfile.getAllPacks())
    }

    @Suppress("unchecked_cast")
    fun <T : Indexable<*, T>?> transformIdentifier(origin: Identifier<*>?): Identifier<T>? {
        return try {
            origin as Identifier<T>
        } catch (e: ClassCastException) {
            writeDriveLog(e)
            null
        }
    }

    @Suppress("unchecked_cast")
    fun <T : Indexable<*, T>?> transformIdentifier(data: String?): Identifier<T>? {
        return try {
            JsonDecoder.decode(JsonParser.parseString(data), Identifier::class.java) as Identifier<T>
        } catch (e: ClassCastException) {
            writeDriveLog(e)
            null
        }
    }

    fun getAnimType(mode: Int): UType {
        return when (mode) {
            1 -> UType.IDLE
            2 -> UType.ATK
            3 -> UType.HB
            4 -> UType.BURROW_DOWN
            5 -> UType.BURROW_MOVE
            6 -> UType.BURROW_UP
            else -> UType.WALK
        }
    }

    fun getPackName(id: String): String {
        return if (id == Identifier.DEF) {
            Identifier.DEF
        } else {
            UserProfile.getUserPack(id)?.desc?.name ?: id
        }
    }

    /**
     * Get music file with specified [Music]
     *
     * @param m Music
     * @return return [File] which points to specified music
     */
    fun getMusicFile(m: Music?): File? {
        m ?: return null

        val ctx = CommonStatic.ctx

        return if(ctx is AContext) {
            ctx.getMusicFile(m)
        } else {
            null
        }
    }

    fun generateIdName(id: Identifier<*>, c: Context?) : String {
        return if(id.pack == Identifier.DEF) {
            (c?.getString(R.string.pack_default) ?: "Default") +" - "+ Data.trio(id.id)
        } else {
            getPackName(id.pack)+" - "+Data.trio(id.id)
        }
    }
}