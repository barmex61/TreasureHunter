<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="64" tileheight="40" tilecount="1" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="attackDamage" type="float" value="1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN_SWORD"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="jumpHeight" type="float" value="3"/>
   <property name="life" type="float" value="3"/>
   <property name="speed" type="float" value="2.5"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image width="64" height="40" source="idle_sword_01.png"/>
  <objectgroup draworder="index" id="3">
   <object id="3" x="24" y="17" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitboxFixture"/>
    </properties>
   </object>
   <object id="12" x="-40" y="31">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="66,0 77,0 77,-1 66,-1"/>
   </object>
   <object id="13" x="26" y="30">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,-12 11,-12 11,0"/>
   </object>
  </objectgroup>
 </tile>
</tileset>
