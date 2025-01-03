import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.selector.ByText;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class test {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test

    void positiveTest() {

        String currentDate = generateDate(4, "dd.MM.yyyy");

        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue("Рязань");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(currentDate);
        $("[data-test-id=name] input").setValue("Василиса Ильинична");
        $("[data-test-id=phone] input").setValue("+79113045566");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + currentDate));

    }

   @Test

    void testWithChooseTheCityFromList() {

       open("http://localhost:9999/");
       $("[data-test-id=city] input").setValue("Мо");
       $(withText("Москва")).click();

       String currentDate = generateDate(4, "dd.MM.yyyy");
       $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
       $("[data-test-id=date] input").sendKeys(currentDate);
       $("[data-test-id=name] input").setValue("Василиса Ильинична");
       $("[data-test-id=phone] input").setValue("+79113045566");
       $("[data-test-id=agreement]").click();
       $("button.button").click();
       $(".notification__content")
               .shouldBe(visible, Duration.ofSeconds(15))
               .shouldHave(Condition.exactText("Встреча успешно забронирована на " + currentDate));

   }

   @Test

   void testWithSelectTheDataFromTheCalendar() {

       open("http://localhost:9999/");

       String selectDate = generateDate(20, "d");
       String dateInAnotherFormat = generateDate(20, "dd.MM.yyyy");
       String todayMonth = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

       char ch1 = todayMonth.charAt(4);
       char ch2 = selectDate.charAt(0);
       int monthOne = Character.getNumericValue(ch1);
       int monthTwo = Character.getNumericValue(ch2);

       $("button").click();

       if (monthOne < monthTwo) {
           $("[data-step ='1']").click();
       }

       $$(".calendar__day").filterBy(text(selectDate)).findBy(exactText(selectDate)).click();

       $("[data-test-id=city] input").setValue("Рязань");
       $("[data-test-id=name] input").setValue("Василиса Ильинична");
       $("[data-test-id=phone] input").setValue("+79113045566");
       $("[data-test-id=agreement]").click();
       $("button.button").click();
       $(".notification__content")
               .shouldBe(visible, Duration.ofSeconds(15))
               .shouldHave(Condition.text("Встреча успешно забронирована на " + dateInAnotherFormat));

   }
}

