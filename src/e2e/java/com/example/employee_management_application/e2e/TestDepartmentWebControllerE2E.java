package com.example.employee_management_application.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.employee_management_application.conn.TestContainerCon;
import com.example.employee_management_application.service.DepartmentService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestDepartmentWebControllerE2E extends TestContainerCon {
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
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}

		departmentService.getAllDepartments()
				.forEach(department -> departmentService.deleteDepartment(department.getDid()));

		// Log database state after deletion
		System.out.println("Departments after deletion: " + departmentService.getAllDepartments().size());
	}

	@Test
	void testCreateDepartment() {
		// Given
		driver.get("http://localhost:" + port + "/departments/create");

		// When
		WebElement nameField = driver.findElement(By.id("dname"));
		nameField.sendKeys("HR");

		WebElement submitButton = driver.findElement(By.id("submitButton"));
		submitButton.click();

		// Then
		driver.get("http://localhost:" + port + "/departments");
		WebElement departmentTable = driver.findElement(By.id("departmentTable"));
		assertThat(departmentTable.getText()).contains("HR");
	}

	@Test
	void testListDepartments() {
		// Given
		driver.get("http://localhost:" + port + "/departments/create");
		WebElement nameField = driver.findElement(By.id("dname"));
		nameField.sendKeys("Sales");
		WebElement submitButton = driver.findElement(By.id("submitButton"));
		submitButton.click();

		// When
		driver.get("http://localhost:" + port + "/departments");

		// Then
		WebElement departmentTable = driver.findElement(By.id("departmentTable"));
		assertThat(departmentTable.getText()).contains("Sales");
	}

	@Test
	void testDeleteDepartment() {
		// Given
		driver.get("http://localhost:" + port + "/departments/create");
		WebElement nameField = driver.findElement(By.id("dname"));
		nameField.sendKeys("IT");
		WebElement submitButton = driver.findElement(By.id("submitButton"));
		submitButton.click();

		// When
		driver.get("http://localhost:" + port + "/departments");

		WebElement departmentRow = driver.findElement(By.xpath("//td[text()='IT']/.."));

		WebElement deleteButton = departmentRow.findElement(By.tagName("button"));
		deleteButton.click();

		// Then
		driver.get("http://localhost:" + port + "/departments");
		WebElement departmentTable = driver.findElement(By.id("departmentTable"));
		assertThat(departmentTable.getText()).doesNotContain("IT");
	}

}
