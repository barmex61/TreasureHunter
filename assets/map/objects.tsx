<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="64" tileheight="64" tilecount="4" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="attackDamage" type="float" value="1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN_SWORD"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="float" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image width="64" height="40" source="../graphics/captain_idle_sword.png"/>
  <objectgroup draworder="index" id="3">
   <object id="3" x="24" y="17" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitboxFixture"/>
    </properties>
   </object>
   <object id="12" x="12" y="30">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="18,0 21,0 21,-1 18,-1"/>
   </object>
   <object id="13" x="26" y="30">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,-12 11,-12 11,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="16">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_LEFT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="51" height="53" source="../graphics/backPalmTrees/back_palm_tree_left_04.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="18" y="2" width="27" height="5">
    <properties>
     <property name="userData" value="platform"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="19">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_REGULAR"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="64" source="../graphics/backPalmTrees/back_palm_tree_regular_03.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="19" y="2" width="27" height="5">
    <properties>
     <property name="userData" value="platform"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="22">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_RIGHT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="52" height="53" source="../graphics/backPalmTrees/back_palm_tree_right_02.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="6" y="2" width="27" height="5">
    <properties>
     <property name="userData" value="platform"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
