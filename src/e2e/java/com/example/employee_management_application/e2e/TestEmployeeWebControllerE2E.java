package com.example.employee_management_application.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.employee_management_application.conn.TestContainerCon;
import com.example.employee_management_application.dto.DepartmentDTO;
import com.example.employee_management_application.service.DepartmentService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestEmployeeWebControllerE2E extends TestContainerCon {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@Autowired
	private DepartmentService departmentService;

	@BeforeEach
	public void setUp() {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		driver = new ChromeDriver(options);

		departmentService.createDepartment(new DepartmentDTO("HR", null));
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	void testCreateEmployee() {
		// Given
		driver.get("http://localhost:" + port + "/employees/create");

		// When
		driver.findElement(By.id("ename")).sendKeys("employee1");
		driver.findElement(By.id("eage")).sendKeys("30");
		driver.findElement(By.id("email")).sendKeys("employee1@example.com");
		driver.findElement(By.id("esalary")).sendKeys("50000.0");

		// Select department
		WebElement departmentDropdown = driver.findElement(By.id("departmentId"));
		departmentDropdown.sendKeys("HR");
		driver.findElement(By.tagName("form")).submit();

		// Then
		driver.get("http://localhost:" + port + "/employees");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));

		// Assert
		WebElement employeeTable = driver.findElement(By.tagName("table"));
		assertThat(employeeTable.getText()).contains("employee1");
	}

}
