'use client';

import React, { useEffect, useState } from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import NavBar from "@/app/components/navbar";
import {getCachesByUser, getCurrentUser} from "@/app/actions/userActions";
import {Card} from "@mui/material";
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Link from "@mui/material/Link";
import {CacheType} from "@/app/components/Cache";
import {useRouter} from "next/navigation";

export default function DashboardPage() {
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState<any>(null); // Adjust to expected response type
  const [error, setError] = useState<string>('');
  const [caches, setCaches] = useState<any[]>([]); // Adjust to expected response type
  const router = useRouter();

  useEffect(() => {
    const fetchUserAndCaches = async () => {
      setLoading(true);
      try {
        const userData = await getCurrentUser(); // Fetch the current user
        setUser(userData); // Set user data
        const userCaches = await getCachesByUser(userData.id); // Fetch the user's caches
        setCaches(userCaches); // Set caches data
      } catch (err) {
        console.error(err); // Log the error for debugging
        setError('Failed to fetch user data or caches'); // Set error message if something goes wrong
      } finally {
        setLoading(false); // Stop loading regardless of success or failure
      }
    };

    fetchUserAndCaches(); // Call the fetch function
  }, []);

  if (loading) {
    return (
      <Container>
        <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error) {
    return (
      <Container>
        <Typography variant="h6" color="error">{error}</Typography> {/* Display error message */}
      </Container>
    );
  }

  const handleNavigation = (cache: CacheType) => {
    router.push(`/pages/map//?lat=${cache.lat}&lng=${cache.lng}`);
  };

  return (
    <div>
      <NavBar />
      <Container className="dashboard-container">
        {user && <Typography variant="h4">Hello, {user.name}</Typography>}
        <Typography variant="h5">Your Caches:</Typography>
        <Box className="cache-list">
          {caches.length > 0 ? (
            caches.map((cache) => (
              <Card key={cache.id} className="cache-card" onClick={() => handleNavigation(cache)}>
                {cache.image && (
                  <CardMedia
                    component="img"
                    height="140"
                    image={cache.image}
                    alt="Cache"
                  />
                )}
                <CardContent>
                  <Typography variant="h6">Title: {cache.title}</Typography>
                  <Typography variant="body2">Description: {cache.desc}</Typography>
                </CardContent>
              </Card>
            ))
          ) : (
            <Typography variant="body1">No caches found.</Typography>
          )}
        </Box>
      </Container>
    </div>
  );
}
