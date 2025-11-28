package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.InvoiceTestSamples.*;
import static com.mycompany.myapp.domain.TextblockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TextblockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Textblock.class);
        Textblock textblock1 = getTextblockSample1();
        Textblock textblock2 = new Textblock();
        assertThat(textblock1).isNotEqualTo(textblock2);

        textblock2.setId(textblock1.getId());
        assertThat(textblock1).isEqualTo(textblock2);

        textblock2 = getTextblockSample2();
        assertThat(textblock1).isNotEqualTo(textblock2);
    }

    @Test
    void invoiceTest() {
        Textblock textblock = getTextblockRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        textblock.setInvoice(invoiceBack);
        assertThat(textblock.getInvoice()).isEqualTo(invoiceBack);

        textblock.invoice(null);
        assertThat(textblock.getInvoice()).isNull();
    }
}
