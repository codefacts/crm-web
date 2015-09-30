package io.crm.web.template.form;

import io.crm.web.template.*;
import io.crm.web.template.input.*;
import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by someone on 30/09/2015.
 */
final public class FormRowBuilder {
    private final List<Template> templates = new ArrayList<>();

    public FormRowBuilder addTemplate(final Template template) {
        templates.add(template);
        return this;
    }

    public FormRowBuilder addTextInput(final InputBuilder<String> b) {
        addTextInput(b.getId(), b.getClasses(), b.getName(), b.getValue(), b.getColumnClasses(), b.getPlaceholder());
        return this;
    }

    public FormRowBuilder addTextInput(final String id, final String classes, final String name, final String value, final String columnWidth, final String placeholder) {
        templates.add(
                new ColumnTemplateBuilder()
                        .setClasses(columnWidth)
                        .setContent(
                                new TextInputTemplateBuilder()
                                        .setId(id)
                                        .setClasses(classes)
                                        .setName(name)
                                        .setValue(value)
                                        .setPlaceholder(placeholder)
                                        .createTextInputTemplate())
                        .createColumnTemplate()
        );
        return this;
    }

    public FormRowBuilder addNumberInput(final InputBuilder<Number> b) {
        addNumberInput(b.getId(), b.getClasses(), b.getName(), b.getValue(), b.getColumnClasses(), b.getPlaceholder());
        return this;
    }

    public FormRowBuilder addNumberInput(final String id, final String classes, final String name, final Number value, final String columnClasses, final String placeholder) {
        templates.add(
                new ColumnTemplateBuilder()
                        .setClasses(columnClasses)
                        .setContent(
                                new NumberTemplateBuilder()
                                        .setId(id)
                                        .setClasses(classes)
                                        .setName(name)
                                        .setPlaceholder(placeholder)
                                        .setValue(value == null ? "" : value.toString())
                                        .createNumberTemplate())
                        .createColumnTemplate()
        );
        return this;
    }

    public FormRowBuilder addDateInput(final InputBuilder<Date> b) {
        addDateInput(b.getId(), b.getClasses(), b.getName(), b.getValue(), b.getColumnClasses(), b.getPlaceholder());
        return this;
    }

    public FormRowBuilder addDateInput(final String id, final String classes, final String name, final Date value, final String columnClasses, String placeholder) {
        templates.add(
                new ColumnTemplateBuilder()
                        .setClasses(columnClasses)
                        .setContent(
                                new DateInputTemplateBuilder()
                                        .setId(id)
                                        .setClasses(classes)
                                        .setName(name)
                                        .setValue(value)
                                        .setPlaceholder(placeholder)
                                        .createDateInputTemplate())
                        .createColumnTemplate()
        );
        return this;
    }

    public FormRowBuilder addRangeInput(final RangeInputBuilder<Number> b) {
        addRangeInput(b.getId(), b.getClasses(), b.getName(), b.getFrom(), b.getTo(), b.getColumnClasses(), b.getPlaceholderFrom(), b.getPlaceholderTo());
        return this;
    }

    public FormRowBuilder addRangeInput(final String id, final String classes, final String name, final Number from, final Number to, final String columnClasses, final String placeholderFrom, final String placeholderTo) {
        templates.add(
                new ColumnTemplateBuilder()
                        .setClasses(columnClasses)
                        .setContent(
                                new RangeTemplateBuilder()
                                        .setId(id)
                                        .setClasses(classes)
                                        .setName(name)
                                        .setFrom(from)
                                        .setTo(to)
                                        .setPlaceholderFrom(placeholderFrom)
                                        .setPlaceholderTo(placeholderTo)
                                        .createRangeTemplate())
                        .createColumnTemplate()
        );
        return this;
    }

    public FormRowBuilder addDateRangeInput(RangeInputBuilder<Date> b) {
        addDateRangeInput(b.getId(), b.getClasses(), b.getName(), b.getFrom(), b.getTo(), b.getColumnClasses(), b.getPlaceholderFrom(), b.getPlaceholderTo());
        return this;
    }

    public FormRowBuilder addDateRangeInput(final String id, final String classes, final String name, final Date from, final Date to, final String columnClasses, final String placeholderFrom, final String placeholderTo) {
        templates.add(
                new ColumnTemplateBuilder()
                        .setClasses(columnClasses)
                        .setContent(
                                new DateRangeTemplateBuilder()
                                        .setId(id)
                                        .setClasses(classes)
                                        .setName(name)
                                        .setFrom(from)
                                        .setTo(to)
                                        .setPlaceholderFrom(placeholderFrom)
                                        .setPlaceholderTo(placeholderTo)
                                        .createDateRangeTemplate())
                        .createColumnTemplate()
        );
        return this;
    }

    public FormRowBuilder addSelectInput(InputBuilder<Map<String, String>> b) {
        addSelectInput(b.getId(), b.getClasses(), b.getName(), b.getValue(), b.getColumnClasses());
        return this;
    }

    public FormRowBuilder addSelectInput(final String id, final String classes, final String name, Map<String, String> valuesMap, final String columnClasses) {
        templates.add(
                new ColumnTemplateBuilder()
                        .setClasses(columnClasses)
                        .setContent(
                                new SelectTemplateBuilder()
                                        .setId(id)
                                        .setClasses(classes)
                                        .setName(name)
                                        .setValuesMap(valuesMap)
                                        .createSelectTemplate())
                        .createColumnTemplate()
        );
        return this;
    }

    public RowTemplate build() {
        return new RowTemplateBuilder()
                .setId(null)
                .setContent(String.join("", templates.stream().map(r -> r.render()).collect(Collectors.toList())))
                .createRowTemplate();
    }
}
