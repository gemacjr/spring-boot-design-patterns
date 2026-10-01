package com.example.designpatterns.creational.prototype;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Asserts the guarantee a correctly implemented Prototype buys: copies are
 * independent all the way down. A shallow copy would fail
 * {@link #mutatingACopysNestedStateLeavesThePrototypeUntouched()} &mdash; which
 * is the entire lesson of this package.
 */
class PrototypeTest {

    @Test
    void copiesCarryTheConfiguredState() {
        ReportTemplate prototype = new ReportTemplate("Quarterly", "A4", new Watermark("DRAFT", 30));

        ReportTemplate copy = prototype.copy();

        assertThat(copy.name()).isEqualTo("Quarterly");
        assertThat(copy.pageSize()).isEqualTo("A4");
        assertThat(copy.watermark().text()).isEqualTo("DRAFT");
    }

    @Test
    void aCopyIsADistinctObject() {
        ReportTemplate prototype = new ReportTemplate("Quarterly", "A4", new Watermark("DRAFT", 30));

        assertThat(prototype.copy()).isNotSameAs(prototype);
    }

    @Test
    void mutatingACopysNestedStateLeavesThePrototypeUntouched() {
        ReportTemplate prototype = new ReportTemplate("Quarterly", "A4", new Watermark("DRAFT", 30));

        ReportTemplate copy = prototype.copy();
        copy.watermark().setText("FINAL");
        copy.watermark().setOpacity(100);

        assertThat(prototype.watermark().text()).isEqualTo("DRAFT");
        assertThat(prototype.watermark().opacity()).isEqualTo(30);
        assertThat(copy.watermark()).isNotSameAs(prototype.watermark());
    }

    @Test
    void theRegistryHandsOutFreshCopiesNotTheStoredPrototype() {
        TemplateRegistry registry = new TemplateRegistry();
        ReportTemplate stored = new ReportTemplate("Invoice", "Letter", new Watermark("UNPAID", 50));
        registry.register("invoice", stored);

        ReportTemplate first = registry.newInstance("invoice");
        ReportTemplate second = registry.newInstance("invoice");

        assertThat(first).isNotSameAs(second).isNotSameAs(stored);
        first.watermark().setText("PAID");
        assertThat(second.watermark().text()).isEqualTo("UNPAID");
    }

    @Test
    void unknownTemplateNameIsRejected() {
        assertThatThrownBy(() -> new TemplateRegistry().newInstance("missing"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("missing");
    }
}
