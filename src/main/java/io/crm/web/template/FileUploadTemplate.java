package io.crm.web.template;

import com.google.common.collect.ImmutableList;
import io.crm.util.SimpleCounter;
import org.watertemplate.Template;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by someone on 01/10/2015.
 */
public class FileUploadTemplate extends Template {
    public FileUploadTemplate(final String statusContent) {
        add("statusContent", statusContent);
        final ArrayList<String> months = new ArrayList<>(Arrays.asList(DateFormatSymbols.getInstance().getMonths()));
        final ArrayList<String> shortMonths = new ArrayList<>(Arrays.asList(DateFormatSymbols.getInstance().getShortMonths()));
        months.remove(months.size() - 1);
        shortMonths.remove(shortMonths.size() - 1);
        SimpleCounter counter = new SimpleCounter(0);
        addCollection("months", months, (m, map) -> {
            final int idx = counter.counter++;
            map.add("value", shortMonths.get(idx));
            map.add("label", months.get(idx));
        });
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (int i = 1; i <= 31; i++) {
            builder.add(i + "");
        }
        addCollection("days", builder.build());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("file-upload.html");
    }
}
