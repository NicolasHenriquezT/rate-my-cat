/*
 * (C) Copyright 2017 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.test.unit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.bonigarcia.Cat;
import io.github.bonigarcia.CatException;
import io.github.bonigarcia.CatRepository;
import io.github.bonigarcia.CatService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (black-box): rating cats")
@Tag("unit")
class RateCatsTest {

    @InjectMocks
    CatService catService;

    @Mock
    CatRepository catRepository;

    // Test data
    Cat dummy = new Cat("dummy", "dummy.png");
    Cat gatoTest = new Cat("gatoTest", "fotoTest.png");   //nuevo
    Cat gatoTest1 = new Cat("gatoTest1", "fotoTest1.png"); //nuevo
    Cat gatoTest2 = new Cat("gatoTest2", "fotoTest2.png"); //nuevo
    
    List<Cat> listaGatos;

    @BeforeAll
    public void funcion(){
        this.listaGatos.add(dummy);
        this.listaGatos.add(gatoTest);
        this.listaGatos.add(gatoTest1);
        this.listaGatos.add(gatoTest2);
    }
    
    
    int stars = 5;
    String comment = "foo";
    
    @ParameterizedTest(name = "Rating cat with {0} stars")
    @ValueSource(doubles = { 0.5, 5 })
    @DisplayName("Correct range of stars test")
    @Tag("functional-requirement-3")
    void testCorrectRangeOfStars(double stars) {
        Cat dummyCat = catService.rateCat(stars, dummy);
        assertThat(dummyCat.getAverageRate(), equalTo(stars));
    }

    @ParameterizedTest(name = "Rating cat with {0} stars")
    @ValueSource(ints = { 0, 6 })
    @DisplayName("Incorrect range of stars test")
    @Tag("functional-requirement-3")
    void testIncorrectRangeOfStars(int stars) {
        assertThrows(CatException.class, () -> {
            catService.rateCat(stars, dummy);
        });
    }

    @Test
    @DisplayName("Rating cats with a comment")
    @Tag("functional-requirement-4")
    void testRatingWithComments() {
        when(catRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(dummy));
        Cat dummyCat = catService.rateCat(stars, comment, 0);
        assertThat(
                catService.getOpinions(dummyCat).iterator().next().getComment(),
                equalTo(comment));
    }

    @Test
    @DisplayName("Rating cats with empty comment")
    @Tag("functional-requirement-4")
    void testRatingWithEmptyComments() {
        when(catRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(dummy));
        Cat dummyCat = catService.rateCat(stars, dummy);
        assertThat(
                catService.getOpinions(dummyCat).iterator().next().getComment(),
                emptyString());
    }

    /*--------------------------------*/

    
    @Test
    @DisplayName("Test nombre correcto gato")
    @Tag("functional-requirement-5")
    void testCorrectCatName() {
        when(catRepository.findById(any(Long.class)))
            .thenReturn(Optional.of(gatoTest));
        String gato1 = catService.findName(10);
        assertEquals("gatoTest", gato1);   
    }

    @Test
    @DisplayName("Test foto correcta gato")
    @Tag("functional-requirement-6")
    void testCorrectPictureName() {
        when(catRepository.findById(any(Long.class)))
            .thenReturn(Optional.of(gatoTest));
        String gato2 = catService.findPicture(gatoTest);
        assertEquals("fotoTest.png", gato2);   
    } 

    @Test
    @DisplayName("Test tamaño correcto de lista de gatos")
    @Tag("functional-requirement-7")
    void testCorrectSize() {
        when(catRepository.findAll())
            .thenReturn(listaGatos);
        assertEquals(2, catService.getCatCount());
    }
}
 