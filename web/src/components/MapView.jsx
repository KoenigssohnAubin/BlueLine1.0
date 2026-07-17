import { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

const AMBULANCE_COLOR = {
  DISPONIBLE: '#2e7d32',
  EN_MISSION: '#1565c0',
  MAINTENANCE: '#757575',
};

const HOSPITAL_COLOR = {
  DISPONIBLE: '#2e7d32',
  SATURATION: '#c0392b',
};

function ambulanceIcon(status) {
  const color = AMBULANCE_COLOR[status] || AMBULANCE_COLOR.DISPONIBLE;
  return L.divIcon({
    className: '',
    html: `<div style="background:${color};width:30px;height:30px;border-radius:50%;border:2px solid #fff;display:flex;align-items:center;justify-content:center;font-size:15px;box-shadow:0 2px 5px rgba(0,0,0,.35)">🚑</div>`,
    iconSize: [30, 30],
    iconAnchor: [15, 15],
  });
}

function hospitalIcon(status) {
  const color = HOSPITAL_COLOR[status] || HOSPITAL_COLOR.DISPONIBLE;
  return L.divIcon({
    className: '',
    html: `<div style="background:${color};width:26px;height:26px;border-radius:6px;border:2px solid #fff;display:flex;align-items:center;justify-content:center;font-size:13px;box-shadow:0 2px 5px rgba(0,0,0,.35)">H</div>`,
    iconSize: [26, 26],
    iconAnchor: [13, 13],
  });
}

export default function MapView({ ambulances = [], hospitals = [], missions = [], height = 480 }) {
  const containerRef = useRef(null);
  const mapRef = useRef(null);
  const layerRef = useRef(null);

  useEffect(() => {
    if (!containerRef.current || mapRef.current) return;
    const map = L.map(containerRef.current, { zoomControl: true }).setView([51.5074, -0.1278], 11);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 19,
    }).addTo(map);
    mapRef.current = map;
    layerRef.current = L.layerGroup().addTo(map);

    return () => {
      map.remove();
      mapRef.current = null;
    };
  }, []);

  useEffect(() => {
    const map = mapRef.current;
    const layer = layerRef.current;
    if (!map || !layer) return;
    layer.clearLayers();

    ambulances
      .filter((a) => a.lat && a.lng)
      .forEach((a) => {
        L.marker([a.lat, a.lng], { icon: ambulanceIcon(a.status) })
          .bindPopup(`<b>${a.code}</b><br>${a.driver || 'Unassigned'}<br>${a.status}`)
          .addTo(layer);
      });

    hospitals
      .filter((h) => h.lat && h.lng)
      .forEach((h) => {
        L.marker([h.lat, h.lng], { icon: hospitalIcon(h.status) })
          .bindPopup(`<b>${h.name}</b><br>${h.address || ''}`)
          .addTo(layer);
      });

    missions.forEach((m) => {
      if (m.aiRoute?.length > 0) {
        L.polyline(
          m.aiRoute.map((p) => [p.lat, p.lng]),
          { color: '#1565c0', weight: 4, opacity: 0.8 }
        ).addTo(layer);
      }
      if (m.pickup?.lat && m.pickup?.lng) {
        L.circleMarker([m.pickup.lat, m.pickup.lng], {
          radius: 6,
          color: '#c0392b',
          fillColor: '#c0392b',
          fillOpacity: 1,
        })
          .bindPopup(`<b>${m.code}</b><br>Pickup: ${m.pickup.address || ''}`)
          .addTo(layer);
      }
    });
  }, [ambulances, hospitals, missions]);

  return <div ref={containerRef} style={{ height, borderRadius: 10, overflow: 'hidden' }} />;
}
