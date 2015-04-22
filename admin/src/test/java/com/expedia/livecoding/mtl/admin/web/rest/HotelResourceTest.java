package com.expedia.livecoding.mtl.admin.web.rest;

import com.expedia.livecoding.mtl.admin.Application;
import com.expedia.livecoding.mtl.admin.domain.Hotel;
import com.expedia.livecoding.mtl.admin.repository.HotelRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HotelResource REST controller.
 *
 * @see HotelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HotelResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";
    private static final String DEFAULT_POSTAL_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_POSTAL_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";
    private static final String DEFAULT_STATE = "SAMPLE_TEXT";
    private static final String UPDATED_STATE = "UPDATED_TEXT";

    @Inject
    private HotelRepository hotelRepository;

    private MockMvc restHotelMockMvc;

    private Hotel hotel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HotelResource hotelResource = new HotelResource();
        ReflectionTestUtils.setField(hotelResource, "hotelRepository", hotelRepository);
        this.restHotelMockMvc = MockMvcBuilders.standaloneSetup(hotelResource).build();
    }

    @Before
    public void initTest() {
        hotel = new Hotel();
        hotel.setName(DEFAULT_NAME);
        hotel.setDescription(DEFAULT_DESCRIPTION);
        hotel.setAddress(DEFAULT_ADDRESS);
        hotel.setCity(DEFAULT_CITY);
        hotel.setPostalCode(DEFAULT_POSTAL_CODE);
        hotel.setCountry(DEFAULT_COUNTRY);
        hotel.setState(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createHotel() throws Exception {
        // Validate the database is empty
        assertThat(hotelRepository.findAll()).hasSize(0);

        // Create the Hotel
        restHotelMockMvc.perform(post("/api/hotels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotel)))
                .andExpect(status().isCreated());

        // Validate the Hotel in the database
        List<Hotel> hotels = hotelRepository.findAll();
        assertThat(hotels).hasSize(1);
        Hotel testHotel = hotels.iterator().next();
        assertThat(testHotel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHotel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHotel.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHotel.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHotel.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testHotel.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testHotel.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void getAllHotels() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotels
        restHotelMockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(hotel.getId().intValue()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.[0].address").value(DEFAULT_ADDRESS.toString()))
                .andExpect(jsonPath("$.[0].city").value(DEFAULT_CITY.toString()))
                .andExpect(jsonPath("$.[0].postalCode").value(DEFAULT_POSTAL_CODE.toString()))
                .andExpect(jsonPath("$.[0].country").value(DEFAULT_COUNTRY.toString()))
                .andExpect(jsonPath("$.[0].state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getHotel() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get the hotel
        restHotelMockMvc.perform(get("/api/hotels/{id}", hotel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hotel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHotel() throws Exception {
        // Get the hotel
        restHotelMockMvc.perform(get("/api/hotels/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHotel() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Update the hotel
        hotel.setName(UPDATED_NAME);
        hotel.setDescription(UPDATED_DESCRIPTION);
        hotel.setAddress(UPDATED_ADDRESS);
        hotel.setCity(UPDATED_CITY);
        hotel.setPostalCode(UPDATED_POSTAL_CODE);
        hotel.setCountry(UPDATED_COUNTRY);
        hotel.setState(UPDATED_STATE);
        restHotelMockMvc.perform(put("/api/hotels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hotel)))
                .andExpect(status().isOk());

        // Validate the Hotel in the database
        List<Hotel> hotels = hotelRepository.findAll();
        assertThat(hotels).hasSize(1);
        Hotel testHotel = hotels.iterator().next();
        assertThat(testHotel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHotel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHotel.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHotel.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHotel.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testHotel.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testHotel.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void deleteHotel() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get the hotel
        restHotelMockMvc.perform(delete("/api/hotels/{id}", hotel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hotel> hotels = hotelRepository.findAll();
        assertThat(hotels).hasSize(0);
    }
}
