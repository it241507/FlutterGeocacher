import type {Metadata} from "next";
import "./globals.css";
import {AppRouterCacheProvider} from "@mui/material-nextjs/v13-appRouter";
import {ThemeProvider} from "@mui/material/styles";
import theme from './theme';
import CssBaseline from '@mui/material/CssBaseline';
import {AuthProvider} from "@/app/auth/authContext";

export const metadata: Metadata = {
  title: "GeoCacher App",
  description: "Find your GeoCaches",
};

export default function RootLayout({children,}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
    <body>
    <AppRouterCacheProvider>
      <AuthProvider>
        <ThemeProvider theme={theme}>
          <CssBaseline/>
          {children}
        </ThemeProvider>
      </AuthProvider>
    </AppRouterCacheProvider>
    </body>
    </html>
  );
}
