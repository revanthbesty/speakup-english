import { AccessibilityProvider } from "../components/accessibility-provider";
import type { ReactNode } from "react";
import type { Metadata } from "next";
import "./globals.css";
import { Nav } from "../components/nav";
export const metadata: Metadata={title:"SpeakUp English",description:"Learn English by using English.",manifest:"/manifest.webmanifest"};
export default function RootLayout({children}:{children:ReactNode}){return <html lang="en"><body><AccessibilityProvider><Nav />{children}</AccessibilityProvider></body></html>}
