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
        Publication pubNoHearingNoScheduler = new Publication();
        pubNoHearingNoScheduler.setClassificationType(ClassificationType.DEADLINE);
        tableTest.put(pubNoHearingNoScheduler, null);

        Publication pubHearingGetDateFromClassifiedDate = new Publication();
        pubHearingGetDateFromClassifiedDate.setClassificationType(ClassificationType.HEARING);
        pubHearingGetDateFromClassifiedDate.setClassifiedDate(LocalDateTime.of(2020, 1, 15, 14, 9, 0));
        pubHearingGetDateFromClassifiedDate.setClippingMatter("lorem ipsum conciliacao para a data de 01/02/2020 as 11:05h dolor sit amet");
        tableTest.put(pubHearingGetDateFromClassifiedDate, "2020-01-15 14:09");

        Publication pubHearingNeedParse = new Publication();
        pubHearingNeedParse.setClassificationType(ClassificationType.HEARING);
        pubHearingNeedParse.setClippingMatter("lorem ipsum conciliacao para a data de 01/02/2020 as 11:05h dolor sit amet");
        tableTest.put(pubHearingNeedParse, "2020-02-01 11:05");

        Publication pubHearingNeedParseWithAccentAndCapitalized = new Publication();
        pubHearingNeedParseWithAccentAndCapitalized.setClassificationType(ClassificationType.HEARING);
        pubHearingNeedParseWithAccentAndCapitalized.setClippingMatter("lorem ipsum Conciliação para a data de 02/05/2022 às 04:07h");
        tableTest.put(pubHearingNeedParseWithAccentAndCapitalized, "2022-05-02 04:07");

        Publication pubHearingNeedParseWithCourtHearingAccentAndCapitalized = new Publication();
        pubHearingNeedParseWithCourtHearingAccentAndCapitalized.setClassificationType(ClassificationType.HEARING);
        pubHearingNeedParseWithCourtHearingAccentAndCapitalized.setClippingMatter("Audiência para a data de 03/12/2001 às 07:08h dolor sit amet");
        tableTest.put(pubHearingNeedParseWithCourtHearingAccentAndCapitalized, "2001-12-03 07:08");

        Publication pubHearingNeedParseWithNoValidParseAtWeekDay = new Publication();
        pubHearingNeedParseWithNoValidParseAtWeekDay.setClassificationType(ClassificationType.HEARING);
        pubHearingNeedParseWithNoValidParseAtWeekDay.setClippingDate(LocalDate.of(2020, 6, 8));
        pubHearingNeedParseWithNoValidParseAtWeekDay.setClippingMatter("lorem dolor sit amet");
        tableTest.put(pubHearingNeedParseWithNoValidParseAtWeekDay, "2020-06-11 00:00");

        Publication pubHearingNeedParseWithNoValidParseAtSaturday = new Publication();
        pubHearingNeedParseWithNoValidParseAtSaturday.setClassificationType(ClassificationType.HEARING);
        pubHearingNeedParseWithNoValidParseAtSaturday.setClippingDate(LocalDate.of(2020, 6, 10));
        pubHearingNeedParseWithNoValidParseAtSaturday.setClippingMatter("lorem dolor sit amet");
        tableTest.put(pubHearingNeedParseWithNoValidParseAtSaturday, "2020-06-15 00:00");

        Publication pubHearingNeedParseWithNoValidParseAtSunday = new Publication();
        pubHearingNeedParseWithNoValidParseAtSunday.setClassificationType(ClassificationType.HEARING);
        pubHearingNeedParseWithNoValidParseAtSunday.setClippingDate(LocalDate.of(2020, 6, 11));
        pubHearingNeedParseWithNoValidParseAtSunday.setClippingMatter("lorem dolor sit amet");
        tableTest.put(pubHearingNeedParseWithNoValidParseAtSunday, "2020-06-15 00:00");
    }

    @Test
    public void schedulerTableTest() {
        tableTest.forEach((test, expected) -> {
            PublicationBusiness.createAppointment(test);
            if (test.getClassificationType().equals(ClassificationType.HEARING)) {
                assertEquals(expected, dateFormat.format(test.getAppointment().getDate()));
            } else {
                assertNull(test.getAppointment());
            }
        });
    }

}
