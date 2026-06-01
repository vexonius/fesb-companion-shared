package dev.etino.fcshared.featuresKotlin.menza.models

val menzaLocations = listOf(
    MenzaLocation(
        name = "Restoran S.O.S.",
        address = "Kopilica ul. 5, Split",
        meniName = MenzaLocationType.KOPILICA,
        cameraName = "",
    ),
    MenzaLocation(
        name = "Medicinski Fakultet",
        address = "Šoltanska 2, Split",
        meniName = MenzaLocationType.MEDICINA,
        cameraName = "b8_27_eb_47_b4_60",
    ),
    MenzaLocation(
        name = "Hostel Spinut",
        address = "Spinutska ulica 2, Split",
        meniName = MenzaLocationType.HOSTEL,
        cameraName = "b8_27_eb_56_1c_fa",
    ),
    MenzaLocation(
        name = "Indeks",
        address = "Svačićeva 8, Split",
        meniName = MenzaLocationType.INDEKS,
        cameraName = "b8_27_eb_82_01_dd",
    ),
    MenzaLocation(
        name = "Kampus",
        address = "Cvite Fiskovića 3, Split",
        meniName = MenzaLocationType.KAMPUS,
        cameraName = "b8_27_eb_aa_ed_1c",
    ),
    MenzaLocation(
        name = "Ekonomski Fakultet",
        address = "Cvite Fiskovića 5, Split",
        meniName = MenzaLocationType.EFST,
        cameraName = "b8_27_eb_d4_79_96",
    ),
    MenzaLocation(
        name = "FGAG",
        address = "Ul. Matice hrvatske 15, Split",
        meniName = MenzaLocationType.FGAG,
        cameraName = "b8_27_eb_ff_a3_7c",
    ),
    MenzaLocation(
        name = "FESB",
        address = "Ruđera Boškovića 32, Split",
        meniName = MenzaLocationType.FESB_STOP,
        cameraName = "",//b8_27_eb_d1_4b_4a",
    ),
    //-------------------------curently closed
    /*MenzaLocation(
        name = "STOP",
        address = "Ruđera Boškovića 32, Split",
        meniName = MenzaLocationType.FESB_STOP,
        cameraName = "b8_27_eb_ac_55_f5",
    ),*/
)

enum class MenzaLocationType(val string: String) {
    KOPILICA("kopilica"),
    MEDICINA("medicina"),
    HOSTEL("hostel"),
    INDEKS("indeks"),
    KAMPUS("kampus"),
    EFST("efst"),
    FGAG("fgag"),

    // FESB_VRH("fesb_vrh"), curently closed
    FESB_STOP("fesb_stop"),
}