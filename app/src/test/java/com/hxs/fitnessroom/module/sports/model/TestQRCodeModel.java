package com.hxs.fitnessroom.module.sports.model;

import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.sports.model.entity.QRCodeBean;

import org.junit.Test;

/**
 * Created by je on 17/09/17.
 */

public class TestQRCodeModel {

    @Test
    public void testQRCodeModel() throws Exception {
        APIResponse<QRCodeBean> qrCodeBeanAPIResponse = QRCodeModel.deQRCode(null, null, null);
        System.out.println(qrCodeBeanAPIResponse.data);
    }
}
