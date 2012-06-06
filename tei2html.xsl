<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:tei="http://www.tei-c.org/ns/1.0"
   version="2.0">
   
   <xsl:output encoding="UTF-8" indent="yes" method="xml"/>
   
   <xsl:strip-space elements="*"/>
   
   <xsl:template match="tei:TEI">
      <html>
         <xsl:apply-templates/>
      </html>
   </xsl:template>
   
   <xsl:template match="tei:text">
      <body>
         <xsl:apply-templates/>
      </body>
   </xsl:template>
   
   <xsl:template match="tei:p | tei:l | tei:div">
      <p>
         <xsl:apply-templates/>
      </p>
   </xsl:template>
   
   <xsl:template match="tei:speaker | tei:head">
      <span>
         <xsl:apply-templates/>
      </span>
   </xsl:template>
   
</xsl:stylesheet>