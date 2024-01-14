package org.proj.utils;

import org.proj.Simulation;
import org.proj.model.SimulationProps;
import org.proj.model.maps.AbstractWorldMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVLogger {
    private String contentToSave;
    private String fileName;

    public CSVLogger() {
        Reset();
    }

    public void Reset() {
        contentToSave = "Day;Animals count;Plants count;Free fields count;Most popular genotype;Average energy level;Average lifespan;Average children count of living animals\n";
    }

    public void LogDay(SimulationProps simulationProps, AbstractWorldMap map, Simulation simulation) {
        String avgLifespan = "---";
        if (simulation.getDeadAnimalsCount() > 0) avgLifespan = String.valueOf(simulation.getAverageLifeSpan());
        contentToSave += simulationProps.getDaysElapsed() + ";" + simulation.getAliveAnimalsCount() + ";" + map.getPlantsCount() + ";" + map.getEmptyCount() + ";" +  simulation.getMostPopularGenotypeStr() + ";" +  simulation.getAverageEnergy() + ";" +  avgLifespan + ";" +  simulation.getAverageChildrenCount() + "\n";
        fileName = simulationProps.getCSVName();
    }

    public void SaveToFile() {
        File file = new File(fileName);
        try {
            FileWriter fr = new FileWriter(file, false);
            fr.write(contentToSave);
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
