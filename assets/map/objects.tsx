<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="objects" tilewidth="72" tileheight="93" tilecount="17" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN_SWORD"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="item" propertytype="Item" value="SWORD"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image source="../graphics/objects/captain_clown_sword.png" width="64" height="40"/>
  <objectgroup draworder="index" id="3">
   <object id="3" x="24" y="17" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="12" x="12" y="30">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="18,0 21,0 21,-1 18,-1"/>
   </object>
   <object id="15" x="26" y="18">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,13 12,13 12,0"/>
   </object>
   <object id="16" x="21" y="12" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
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
  <image source="../graphics/objects/back_palm_tree_left.png" width="51" height="53"/>
 </tile>
 <tile id="19">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_REGULAR"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/back_palm_tree_regular.png" width="64" height="64"/>
 </tile>
 <tile id="22">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_RIGHT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/back_palm_tree_right.png" width="52" height="53"/>
 </tile>
 <tile id="25">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="FRONT_PALM_TREE"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/front_palm_tree.png" width="39" height="32"/>
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
  <image source="../graphics/objects/palm_tree_bottom_left.png" width="64" height="32"/>
 </tile>
 <tile id="27">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_RIGHT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/palm_tree_bottom_right.png" width="64" height="30"/>
 </tile>
 <tile id="26">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_REGULAR"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/palm_tree_bottom_regular.png" width="32" height="32"/>
 </tile>
 <tile id="29">
  <properties>
   <property name="animFrameDuration" type="float" value="0.2"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="CHEST"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/chest.png" width="64" height="35"/>
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
  <image source="../graphics/objects/flag.png" width="34" height="93"/>
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
  <image source="../graphics/objects/ship_helm.png" width="31" height="32"/>
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
   <property name="damage" type="int" value="1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SPIKES"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/spikes.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="9" x="8" y="16" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="12" x="16" y="19" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="13" x="24" y="17" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="0.4"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="14" x="0" y="21" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="34">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="COLLECTABLE"/>
   <property name="gameObject" propertytype="GameObject" value="SWORD"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image source="../graphics/objects/sword.png" width="20" height="20"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="18" y="13">
    <polygon points="0,0 2,-2 2,-5 -10,-5 -12,-6 -14,-6 -14,-4 -16,-4 -16,-3 -17,-3 -17,0 -14,0 -14,1 -12,1 -12,0 -11,0 -11,-1 -10,-1 -10,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="35">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="item" propertytype="Item" value="NONE"/>
   <property name="jumpHeight" type="float" value="1.1"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image source="../graphics/objects/captain_clown.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="24" y="16" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="12" y="30">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="18,0 21,0 21,-1 18,-1"/>
   </object>
   <object id="3" x="26" y="17">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,13 12,13 12,0"/>
   </object>
   <object id="4" x="21" y="11" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="48">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="aiWanderRadius" type="float" value="3"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="gameObject" propertytype="GameObject" value="CRABBY"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2.5"/>
  </properties>
  <image source="../graphics/objects/crabby.png" width="72" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="27" y="13" width="20" height="16">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="29" y="15">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,13 16,13 16,0"/>
   </object>
   <object id="4" x="11" y="28">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="24,0 28,0 28,-1 24,-1"/>
   </object>
   <object id="5" x="26" y="9" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="49">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="aiWanderRadius" type="float" value="4"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="gameObject" propertytype="GameObject" value="FIERCE_TOOTH"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image source="../graphics/objects/fierce_tooth.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="9" y="7" width="17" height="20">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="11" y="9">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,17 13,17 13,0"/>
   </object>
   <object id="3" x="-15" y="26">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="30,0 35,0 35,-1 30,-1"/>
   </object>
   <object id="4" x="6" y="6" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="50">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="aiWanderRadius" type="float" value="7"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="gameObject" propertytype="GameObject" value="PINK_STAR"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAttack" type="bool" value="true"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="3"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image source="../graphics/objects/pink_star.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10" y="10" width="14" height="18">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="11" y="12">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,15 12,15 12,0"/>
   </object>
   <object id="3" x="-8" y="27">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="23,0 28,0 28,-1 23,-1"/>
   </object>
   <object id="4" x="6" y="8" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
