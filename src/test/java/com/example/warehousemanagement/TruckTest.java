package com.example.warehousemanagement;

import com.example.warehousemanagement.dto.TruckDto;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WarehousemanagementApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class TruckTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        System.out.println("Truck - Test Start Up");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void cleanData() {
        System.out.println("Test.tearDown Truck");
    }

    @Test
    @Tag("01_Get_All_Trucks")
    public void test_01_01() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/truck/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @Tag("02_Get_Truck_By_Id")
    public void test_01_02() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/truck/id")
                        .param("truckId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @Tag("03_Add_New_Truck")
    public void test_01_03() throws Exception {
        TruckDto dto = new TruckDto();
        dto.setLicensePlate("Test");
        dto.setChassisNumber("Test");
        dto.setEnabled(true);

        Gson gson = new Gson();
        String json = gson.toJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/truck/add")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is(200));
    }

    @Test
    @Tag("04_Disable_Truck")
    public void test_01_04() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/truck/disable")
                        .param("chassisNumber", "xcdsjiqoe1234")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}
