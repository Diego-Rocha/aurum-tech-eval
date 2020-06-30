package io.diego.aurum.tech.eval.business;

import io.diego.aurum.tech.eval.model.entity.Publication;
import io.diego.aurum.tech.eval.model.enums.ClassificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PublicationBusinessTest {

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Map<Publication, String> tableTest = new HashMap<>();

    {
        Publication pubNoHearingNoScheduler = Publication.builder()
                .classificationType(ClassificationType.DEADLINE)
                .build();
        tableTest.put(pubNoHearingNoScheduler, null);

        Publication pubHearingGetDateFromClassifiedDate = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .classifiedDate(LocalDateTime.of(2020, 1, 15, 14, 9, 0))
                .clippingMatter("lorem ipsum conciliacao para a data de 01/02/2020 as 11:05h dolor sit amet")
                .build();
        tableTest.put(pubHearingGetDateFromClassifiedDate, "2020-01-15 14:09");

        Publication pubHearingNeedParse = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .clippingMatter("lorem ipsum conciliacao para a data de 01/02/2020 as 11:05h dolor sit amet")
                .build();
        tableTest.put(pubHearingNeedParse, "2020-02-01 11:05");

        Publication pubHearingNeedParse2 = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .clippingMatter("lorem ipsum conciliacao para a data de 20 de janeiro de 2020 as 09:03h dolor sit amet")
                .build();
        tableTest.put(pubHearingNeedParse2, "2020-01-20 09:03");

        Publication pubHearingNeedParseWithAccentAndCapitalized = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .clippingMatter("lorem ipsum Conciliação para a data de 02/05/2022 às 04:07h")
                .build();
        tableTest.put(pubHearingNeedParseWithAccentAndCapitalized, "2022-05-02 04:07");

        Publication pubHearingNeedParseWithCourtHearingAccentAndCapitalized = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .clippingMatter("Audiência para a data de 03/12/2001 às 07:08h dolor sit amet")
                .build();
        tableTest.put(pubHearingNeedParseWithCourtHearingAccentAndCapitalized, "2001-12-03 07:08");

        Publication pubHearingNeedParseWithNoValidParseAtWeekDay = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .clippingDate(LocalDate.of(2020, 6, 8))
                .clippingMatter("lorem dolor sit amet")
                .build();
        tableTest.put(pubHearingNeedParseWithNoValidParseAtWeekDay, "2020-06-11 00:00");

        Publication pubHearingNeedParseWithNoValidParseAtSaturday = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .clippingDate(LocalDate.of(2020, 6, 10))
                .clippingMatter("lorem dolor sit amet")
                .build();
        tableTest.put(pubHearingNeedParseWithNoValidParseAtSaturday, "2020-06-15 00:00");

        Publication pubHearingNeedParseWithNoValidParseAtSunday = Publication.builder()
                .classificationType(ClassificationType.HEARING)
                .clippingDate(LocalDate.of(2020, 6, 11))
                .clippingMatter("lorem dolor sit amet")
                .build();
        tableTest.put(pubHearingNeedParseWithNoValidParseAtSunday, "2020-06-15 00:00");
    }

    @Test
    public void schedulerTableTest() {
        tableTest.forEach((test, expected) -> {
            PublicationBusiness.beforeSave(test);
            if (test.getClassificationType().equals(ClassificationType.HEARING)) {
                assertEquals(expected, dateFormat.format(test.getAppointment().getDate()));
            } else {
                assertNull(test.getAppointment());
            }
        });
    }

}
