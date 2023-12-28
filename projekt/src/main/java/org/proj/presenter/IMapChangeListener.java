package org.proj.presenter;

import org.proj.model.maps.AbstractWorldMap;

public interface IMapChangeListener {
    void mapChanged(AbstractWorldMap map, String message);
}
