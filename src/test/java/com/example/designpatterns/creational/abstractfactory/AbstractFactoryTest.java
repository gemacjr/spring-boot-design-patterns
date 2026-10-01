package com.example.designpatterns.creational.abstractfactory;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee Abstract Factory buys and Factory Method does not:
 * every part produced by one factory belongs to the same family. You cannot
 * accidentally assemble a PDF header onto an HTML body.
 */
class AbstractFactoryTest {

    @Test
    void everyPartFromOneFactoryBelongsToTheSameFamily() {
        for (ReportFactory factory : List.of(new PdfReportFactory(), new HtmlReportFactory())) {
            assertThat(factory.createHeader().format()).isEqualTo(factory.format());
            assertThat(factory.createBody().format()).isEqualTo(factory.format());
            assertThat(factory.createFooter().format()).isEqualTo(factory.format());
        }
    }

    @Test
    void theAssemblerIsWrittenOnceAndWorksForEveryFamily() {
        List<String> rows = List.of("alpha", "beta");

        String pdf = new ReportAssembler().assemble(new PdfReportFactory(), "Sales", rows);
        String html = new ReportAssembler().assemble(new HtmlReportFactory(), "Sales", rows);

        assertThat(pdf).contains("Sales").contains("alpha").contains("beta");
        assertThat(html).contains("Sales").contains("alpha").contains("beta");
        assertThat(html).contains("<h1>");
        assertThat(pdf).doesNotContain("<h1>");
    }
}
