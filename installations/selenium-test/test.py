import time
import unittest

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By

class Tester(unittest.TestCase):
    def setUp(self): # This is automatically executed by unittes before test methods

        options = webdriver.ChromeOptions()
        self.driver=webdriver.Remote( # This allows to connect to a webdriver (and therefore control its associated browser) thru a GRID
            command_executor="http://172.31.0.67:4444"#,
            #desired_capabilities={
            #    "browserName": "chrome"
            #},
            , options=options

        )
        self.base_url = "http://172.31.0.67:8081/webapp/"
        self.driver.implicitly_wait(30)
        self.verificationErrors = []
        self.accept_next_alert = True

    def test1(self): # Our unique test method
        driver = self.driver
        driver.get(self.base_url)
        time.sleep(3)
        text=driver.find_element(By.XPATH,"//h2").text
        driver.save_screenshot("screenshot.png") # this is gonna execute in background.
        self.assertEqual("Hello Jenkins!",text)

    def tearDown(self):# This is automatically executed by unittes after test methods
        self.driver.quit()

unittest.main() # This execute all the test methods in this class