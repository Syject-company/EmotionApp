package com.syject.eqally.data.enums

import com.syject.eqally.R
import java.io.Serializable

enum class TestEnums(
    val descriptionArrayResourceID: Int,
    val testNameResourceID: Int,
    val testPackageName: String,
    val defaultPrice: Double,
    val maxImages: Int,
    val maxEmotions: Int,
    val testImagePath: String,
    val headerTextResourceID: Int,
    val skuID: String,
    val questionCount: Int
) : Serializable {

    DEMO(
        R.array.demo_test_description,
        R.string.Test_Activity_demo_test_name,
        ("DEMO.1"),
        (0.0),
        1,
        2,
        "/1_2_0.enc",
        R.string.Test_header_free,
        "",
        13
    ),
    FULL(
        R.array.full_test_description,
        R.string.Test_Activity_full_test_name,
        ("BPP.1"),
        (9.99),
        2,
        3,
        "/12_3_0.enc",
        R.string.Test_header_discount_33,
        "bpp.1",
        13
    ),
    PROFILE(
        R.array.full_test_description,
        R.string.Test_Activity_profile_test_name,
        ("BFP.1"),
        (9.99),
        2,
        3,
        "/10_7_0.enc",
        R.string.Test_header_coming_soon,
        "bfp.1",
        13
    );
}