package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockpTS;
  private TorpedoStore mocksTS;

  @BeforeEach
  public void init(){
    mockpTS=mock(TorpedoStore.class);
    mocksTS=mock(TorpedoStore.class);
    this.ship = new GT4500(mockpTS, mocksTS);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockpTS.fire(1)).thenReturn(true);
    // Act
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert 
    verify(mockpTS,times(1)).fire(1);
    verify(mocksTS,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mocksTS.fire(1)).thenReturn(true);
    // Act
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockpTS,times(1)).fire(1);
    verify(mocksTS,times(1)).fire(1);
  }

  //Single, előtte a primary torpedoval 
  //egy loves vegrehajtasa (ekkor a masodikkal kell loni a masodik single hívaskor, tehat 2 single hivas utan 1-1
  //kell, hogy legyen a fire hivasok szama.
  @Test
  public void fireBothTorpedo_Single_Success(){
    // Arrange
    when(mocksTS.fire(1)).thenReturn(true);
    //Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    verify(mockpTS,times(1)).fire(1);
    verify(mocksTS,times(1)).fire(1);
  }

  //3 single kilovese, igy 2szer kell a primaryt, egyszer a secondaryt hasznalni.
  @Test
  public void fire_First_Second_First_Torpedo_Single_Success(){
    // Arrange
    when(mockpTS.fire(anyInt())).thenReturn(true);
    when(mocksTS.fire(anyInt())).thenReturn(true);
    //Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    verify(mockpTS,times(2)).fire(anyInt());
    verify(mocksTS,times(1)).fire(1);
  }

  //Single utana All kilovese, utana 1 single, ekkor 2szer kell a primaryt, 2szer a secondaryt hasznalni.
  @Test
  public void fire_All_Then_Single_Torpedo_Success(){
    //Arrange
    when(mockpTS.fire(anyInt())).thenReturn(true);
    when(mocksTS.fire(anyInt())).thenReturn(true);
    //Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.ALL);
    ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    verify(mockpTS,times(2)).fire(anyInt());
    verify(mocksTS,times(2)).fire(anyInt());
  }

  //Single kilovese, sikertelene, arrangbe beallitva, hogy a loves ne sikeruljon egyik torpedostore-nal se.
  @Test
  public void fire_Single_Failed(){
    //Arrange
    when(mockpTS.fire(anyInt())).thenReturn(false);
    when(mocksTS.fire(anyInt())).thenReturn(false);

    //Assert
    assertEquals(false, ship.fireTorpedo(FiringMode.SINGLE));

  }

  //Loves masodikkal, majd megegyszer a masodikkal, mivel az elso ures.
  @Test
  public void second_fired_Twice_In_a_Row(){
    //Arrange
    when(mockpTS.isEmpty()).thenReturn(true);
    when(mocksTS.fire(anyInt())).thenReturn(true);
    //Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    verify(mockpTS,times(0)).fire(anyInt());
    verify(mocksTS,times(2)).fire(anyInt());

  }

}
