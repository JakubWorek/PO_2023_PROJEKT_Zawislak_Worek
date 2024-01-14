package org.proj.model;

import org.junit.jupiter.api.Test;
import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.EMutationStyle;
import org.proj.model.maps.EMapType;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationPropsTest {
    @Test
    public void testEverything(){
        SimulationProps simulationProps = new SimulationProps(10, 10, 5, 5, 5, 5, 5, 5, 5, EMoveStyle.FULLY_PREDESTINED , EMutationStyle.FULLY_RANDOM , EMapType.GLOBE, 5, 5, 5, 5, true, "test", 5, 0, 2);
        assertEquals(10, simulationProps.getMapWidth());
        assertEquals(10, simulationProps.getMapHeight());
        assertEquals(5, simulationProps.getEquatorHeight());
        assertEquals(5, simulationProps.getStartAnimalCount());
        assertEquals(5, simulationProps.getStartPlantCount());
        assertEquals(5, simulationProps.getSpawnPlantPerDay());
        assertEquals(5, simulationProps.getStartEnergy());
        assertEquals(5, simulationProps.getMaxEnergy());
        assertEquals(5, simulationProps.getPlantEnergy());
        assertEquals(EMoveStyle.FULLY_PREDESTINED , simulationProps.getMoveStyle());
        assertEquals(EMutationStyle.FULLY_RANDOM , simulationProps.getMutationStyle());
        assertEquals(5, simulationProps.getGenesCount());
        assertEquals(5, simulationProps.getEnergyLevelNeededToReproduce());
        assertEquals(5, simulationProps.getEnergyLevelToPassToChild());
        assertEquals(5, simulationProps.getMoveEnergy());
        assertEquals(0, simulationProps.getDaysElapsed());
        assertTrue(simulationProps.shouldSaveCSV());
        assertEquals("test", simulationProps.getCSVName());
        assertEquals(5, simulationProps.getSimulationStep());
        assertEquals(0, simulationProps.getMinMutation());
        assertEquals(2, simulationProps.getMaxMutation());
    }
}
