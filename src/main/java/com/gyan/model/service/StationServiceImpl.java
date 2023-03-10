package com.gyan.model.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyan.beans.Station;
import com.gyan.model.persistence.StationDao;

@Service
public class StationServiceImpl implements StationService {
	@Autowired
	private StationDao stationDao;

	@Override
	public List<Station> getStations() {	
		return stationDao.findAll();
	}

	@Override
	public Station addStation(String name) {
		return stationDao.save(new Station(name));
	}

}
