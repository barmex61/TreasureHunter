<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="64" tileheight="40" tilecount="1" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN_SWORD"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="jumpHeight" type="float" value="5"/>
   <property name="speed" type="float" value="5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="1"/>
  </properties>
  <image width="64" height="40" source="idle_sword_01.png"/>
  <objectgroup draworder="index" id="3">
   <object id="3" x="24" y="17" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitboxFixture"/>
    </properties>
   </object>
   <object id="7" x="26" y="19" width="11" height="12">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
   </object>
   <object id="8" x="25" y="33">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polyline points="0,0 13,0 13,-2 0,-2 0,0"/>
   </object>
  </objectgroup>
 </tile>
</tileset>
