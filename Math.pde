float cartesian2polarAngle (float x, float y){ 
  float angle = (atan2(y,x) * 180/PI); 
  return angle; 
} 