import groovy.xml.MarkupBuilder
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver

/**
 * Created by oreny on 8/11/15.
 * This is a simple example that takes the latest news titiles regarding the stock ticker AAPL
 * from Google Finance, places them into a simple XML format and prints the XML format out.
 */
class Main {
    public static void main(String[] args) {

        String ticker = "AAPL"
        WebDriver driver
        try {
            println "Initializing Webdriver"
            driver = new FirefoxDriver()

            println "navigating to Google Finance"
            driver.get("http://www.google.com/finance")

            println "Writing the stock ticker symbol $ticker into the search box"
            WebElement searchBox = driver.findElementById("gbqfq")
            searchBox.sendKeys(ticker)
            searchBox.sendKeys(Keys.RETURN)

            //wait 5 seconds to make sure news are loaded
            sleep 5000

            //find the news title based on a CSS selector
            def newsTitles = driver.findElementsByCssSelector(".news-item .title")

            println "${newsTitles.size()} news items found. creating XML"
            StringWriter xmlWriter = new StringWriter()
            def xmlResult = new MarkupBuilder(xmlWriter).titles("ticker":ticker){
                newsTitles.each{titleElement->
                    title(titleElement.text)
                }
            }
            println "Done, here's the XML"
            println xmlWriter.toString()
        } catch (Throwable t) {
            t.printStackTrace()
        } finally {
            driver.quit()
        }

    }
}
