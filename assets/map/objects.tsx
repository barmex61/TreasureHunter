<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="64" tileheight="93" tilecount="12" columns="0">
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
   <property name="life" type="int" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image width="64" height="40" source="../graphics/captain_clown.png"/>
  <objectgroup draworder="index" id="3">
   <object id="3" x="24" y="17" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="12" x="12" y="30">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="18,0 21,0 21,-1 18,-1"/>
   </object>
   <object id="15" x="26" y="19">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,11 11,11 11,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="16">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_LEFT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="51" height="53" source="../graphics/back_palm_tree_left.png"/>
 </tile>
 <tile id="19">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_REGULAR"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="64" source="../graphics/back_palm_tree_regular.png"/>
 </tile>
 <tile id="22">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_RIGHT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="52" height="53" source="../graphics/back_palm_tree_right.png"/>
 </tile>
 <tile id="25">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="FRONT_PALM_TREE"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="39" height="32" source="../graphics/front_palm_tree.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="4" y="3" width="32" height="5">
    <properties>
     <property name="userData" value="platform"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="28">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_LEFT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="32" source="../graphics/palm_tree_bottom_left.png"/>
 </tile>
 <tile id="27">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_RIGHT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="30" source="../graphics/palm_tree_bottom_right.png"/>
 </tile>
 <tile id="26">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_REGULAR"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/palm_tree_bottom_regular.png"/>
 </tile>
 <tile id="29">
  <properties>
   <property name="animFrameDuration" type="float" value="0.2"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="CHEST"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="35" source="../graphics/chest.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="19" y="11" width="28" height="23">
    <properties>
     <property name="userData" value="chest"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="30">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="FLAG"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="34" height="93" source="../graphics/flag.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="4" height="93">
    <properties>
     <property name="userData" value="flag"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="31">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SHIP_HELM"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="31" height="32" source="../graphics/ship_helm.png"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="4" y="4" width="23" height="23">
    <properties>
     <property name="userData" value="shipHelm"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="32">
  <properties>
   <property name="bodyDamage" type="int" value="1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SPIKES"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/spikes.png"/>
  <objectgroup draworder="index" id="2">
   <object id="9" x="8" y="16" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="hitbox"/>
    </properties>
    <ellipse/>
   </object>
   <object id="12" x="16" y="19" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="hitbox"/>
    </properties>
    <ellipse/>
   </object>
   <object id="13" x="24" y="17" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="hitbox"/>
    </properties>
    <ellipse/>
   </object>
   <object id="14" x="0" y="21" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="hitbox"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
