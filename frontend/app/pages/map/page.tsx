'use client';

import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {MapContainer, Marker, Popup, TileLayer, useMap, useMapEvents} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.webpack.css';
import 'leaflet-defaulticon-compatibility';
import { useEffect, useRef, useState } from 'react'
import {loadImage, login, setCache, setComment} from "@/app/actions/userActions";
import {bounds, LatLngBounds, LatLngBoundsLiteral} from "leaflet";
import Container from "@mui/material/Container";
import NavBar from "@/app/components/navbar";
import {CacheType} from "@/app/components/Cache";
import {useSearchParams} from "next/navigation";
import {normalizeCatchAllRoutes} from "next/dist/build/normalize-catchall-routes";
import axios from "axios";
import {CommentType} from "@/app/components/Comment";



let center = {lat: 48.213753110136466, lng: 15.631694122972178};
const worldBounds: LatLngBoundsLiteral = [
  [-90, -180], // Southwest corner
  [90, 180]    // Northeast corner
];

export default function MapPage() {

  const [open, setOpen] = useState(false);
  const [latitude, setLatitude] = useState(0);
  const [longitude, setLongitude] = useState(0);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [image, setImage] = useState('');
  const [error, setError] = useState('');
  const [caches, setCaches] = useState<CacheType[]>([]);
  const [mapReady, setMapReady] = useState(false);
  const searchParams = useSearchParams();
  const [message, setMessage] = useState('');
  const [activeCacheId, setActiveCacheId] = useState(0);

  const urlL = searchParams.get('search')

  const mapRef = useRef<L.Map | null>(null);



  useEffect(() => {
    const urlLat: number = Number(searchParams.get('lat'));
    const urlLng: number = Number(searchParams.get('lng'));
    if (urlLat && urlLng) {
      center = {lat: urlLat, lng: urlLng}
    }
    import('leaflet');
  }, [searchParams]);

  useEffect(() => {
    if (mapReady && mapRef.current) {
      mapRef.current.setView(center, mapRef.current.getZoom());
      sendBounds();
    }
  }, [mapReady]);


  async function sendBounds(){
    if (mapRef.current) {
      const bounds = mapRef.current.getBounds();
      const northEast = bounds.getNorthEast();
      const southWest = bounds.getSouthWest();
      try {
        const response = await fetch(`http://localhost:8083/api/caches/map/${northEast.lat}/${northEast.lng}/${southWest.lat}/${southWest.lng}`);
        const data: CacheType[] = await response.json();

        for (const cache of data) {
          if (cache.imageFilename) {
            const blob = await loadImage(cache.imageFilename);
            cache.image = URL.createObjectURL(blob);
          }
        }

        setCaches(data);
      } catch (error) {
        console.error('Error fetching caches:', error);
      }
    }

  }

  function MyComponent() {

    const map = useMapEvents({
      click: (e) => {
        setLatitude(e.latlng.lat);
        setLongitude(e.latlng.lng);
        map.locate()
        setOpen(true);
      },
      locationfound: (location) => {
        console.log('location found:', location)
      },
      moveend: async () => {
        await sendBounds()
      }
    });
    return null;
  }

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setMessage('');
    setOpen(false);
  };

  const handleSubmit = async (e:any) => {
    e.preventDefault();
    console.log('Cache submitted with:', { title, description, latitude, longitude });

    try {
      const base64String = image.split(',')[1];
      await setCache(longitude, latitude, title, description, base64String);
      console.log('Cache set successful');
      handleClose();
      sendBounds();
    } catch (err: any) {
      setError(err.message || 'Something went wrong');
    }
  };

  const formatDate = (d: Date) => {
    return `${d.getDay() < 10 ? `0${d.getDay()}` : d.getDay()}.${d.getMonth()+1 < 10 ? `0${d.getMonth()+1}` : d.getMonth()+1}.${d.getFullYear()} ${d.getHours() < 10 ? `0${d.getHours()}` : d.getHours()}:${d.getMinutes() < 10 ? `0${d.getMinutes()}` : d.getMinutes()}`
  }

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      console.log(file.type)

      if (file.type !== 'image/jpeg' && file.type !== 'image/jpg') {
        return;
      }

      if (file.size > 5 * 1024 * 1024) {
        alert('Image must be smaller than 5 MB');
        return;
      }
      const reader = new FileReader();
      reader.onloadend = () => {
        const base64 = reader.result as string;
        setImage(base64);
      };
      reader.readAsDataURL(file);
    }
  }

  const postComment = async (cacheId: number) => {
    // console.log(`bevor: ${cacheId}`)
    try {
      let tempCaches = caches.slice();
      console.log("caches bevor")
      console.log(caches);
      const comment: CommentType = await setComment(message, cacheId);
      const index = tempCaches.findIndex((c) => c.id === cacheId);
      tempCaches[index].comments.push(comment);
      setCaches(tempCaches);
      setMessage('');
    } catch (e) {
        console.error('Error posting comment:', e);
    }
  }

  return (
    <div style={{height: '100vh', width: '100vw'}}>
      <NavBar></NavBar>
      <MapContainer
        worldCopyJump={false}
        center={center}
        zoom={10}
        style={{height: '100%', width: '100%'}}
        maxBounds={worldBounds}
        maxBoundsViscosity={1.0}
        minZoom={3}
        maxZoom={18}
        ref={mapRef}
        whenReady={() => setMapReady(true)}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://osm.org/copyright">OpenStreetMap</a> contributors'
          noWrap={true}
        />
        {caches.map((cache) => (
          <Marker key={cache.id} position={[cache.lat, cache.lng]}>
            <Popup
              minWidth={600}
              maxWidth={600}
              maxHeight={400}
            >
              <div className="popup-content">
                <div>
                  <strong>{cache.title}</strong><br/>
                  <p>{cache.desc}</p>
                  <p>{cache.userName}</p>
                  {cache.image && <img src={cache.image} alt="Cache" style={{width: 'auto', height:'250px', objectFit:'cover'}} />}
                </div>
                <div className={'comment-wrapper'}>
                  <div className={'comment-display'}>
                    {cache.comments.map((comment) => (
                        <div key={`comment-${comment.id}`} className={'comment-body'}>
                          <p className={'comment__user-name'}>{comment.user.name}</p>
                          <p className={'comment__message'}>{comment.message}</p>
                          <p className={'comment__date'}>{formatDate(new Date(comment.timeStamp))}</p>
                        </div>
                    ))}
                  </div>
                  <div className={'send-message'}>
                    <form>
                      <TextField
                          margin="dense"
                          id="message"
                          label="Message"
                          type="text"
                          fullWidth
                          value={message}
                          onChange={(e) => setMessage(e.target.value)}/>
                      <button onClick={(e) => {e.preventDefault(); postComment(cache.id);}}>Send</button>
                    </form>
                  </div>
                </div>
              </div>
            </Popup>
          </Marker>
        ))}
        <MyComponent></MyComponent>
      </MapContainer>
      material dialog state open with click change variable
        <Dialog
          open={open}
          onClose={handleClose}
          PaperProps={{
            component: 'form',
            onSubmit: (event: React.FormEvent<HTMLFormElement>) => {
              event.preventDefault();
              handleClose();
            },
          }}
        >
          <DialogTitle>Submit a cache!</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Put in your caches here.
            </DialogContentText>
            <TextField
              autoFocus
              margin="dense"
              id="latitude"
              label="Latitude"
              type="text"
              fullWidth
              value={latitude}
              InputProps={{ readOnly: true }}
              onChange={(e) => setLatitude(parseFloat(e.target.value))}
            />
            <TextField
              margin="dense"
              id="longitude"
              label="Longitude"
              type="text"
              fullWidth
              value={longitude}
              InputProps={{ readOnly: true }}
              onChange={(e) => setLongitude(parseFloat(e.target.value))}
            />
            <TextField
              autoFocus
              margin="dense"
              id="title"
              label="Cache Title"
              type="text"
              fullWidth
              onChange={(e) => setTitle(e.target.value)}
            />
            <TextField
              autoFocus
              margin="dense"
              id="description"
              label="Cache Description"
              type="text"
              fullWidth
              onChange={(e) => setDescription(e.target.value)}
            />
            <Button
              id="image"
              variant="contained"
              component="label"
              sx={{ mt: 2 }}
            >
              Upload Image
              <input
                type="file"
                accept="image/jpg"
                hidden
                onChange={handleImageUpload}
              />
            </Button>
            {image && (
              <img
                src={image}
                alt="Uploaded"
                style={{ width: 'auto', marginTop: '10px', height: '200px', objectFit: 'cover' }}
              />
            )}
          </DialogContent>

          <DialogActions>
            <Button onClick={handleClose}>Cancel</Button>
            <Button type="submit" onClick={handleSubmit}>Submit</Button>
          </DialogActions>
        </Dialog>
    </div>
  );
}