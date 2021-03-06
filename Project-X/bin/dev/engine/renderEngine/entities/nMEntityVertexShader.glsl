#version 400 core

const int maxLightCount = 4;				// Multiple Lights

in vec3 position;
in vec2 textureCoords;
in vec3 normal;								// Per-Pixel Lighting
in vec3 tangent;							// Normal Mapping

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPositionEyeSpace[maxLightCount];	// Per-Pixel Lighting
uniform float useFakeLighting;				// For 2D Objects
uniform float atlasNumberOfRows;			// Texture Atlases
uniform vec2 atlasOffsets;					// Texture Atlases
uniform float densityOfFog;					// Fog
uniform float gradientOfFog;				// Fog
uniform vec4 clipPlane;

out vec2 pass_textureCoords;
out vec3 toLightVector[maxLightCount];		// Per-Pixel Lighting
out vec3 toCameraVector;					// Specular Lighting
out float visibility;						// Fog


void main(){

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);

	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;

	pass_textureCoords = (textureCoords / atlasNumberOfRows) + atlasOffsets;

	vec3 actualNormal = normal;
	if(useFakeLighting > 0.5)
		actualNormal = vec3(0.0, 1.0, 0.0);

	vec3 surfaceNormal = (modelViewMatrix * vec4(normal,0.0)).xyz;													// Per-Pixel Lighting
	vec3 norm = normalize(surfaceNormal);																			// Normal Mapping
	vec3 tangent = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);											// Normal Mapping
	vec3 bitang = normalize(cross(norm, tangent));																	// Normal Mapping
	mat3 toTangentSpace = mat3(																						// Normal Mapping
			tangent.x,		bitang.x,	norm.x,																		// Normal Mapping
			tangent.y,		bitang.y,	norm.y,																		// Normal Mapping
			tangent.z,		bitang.z,	norm.z																		// Normal Mapping
	);

	for(int i = 0 ; i < maxLightCount ; i++){																		// Per-Pixel Lighting
		toLightVector[i] = toTangentSpace * (lightPositionEyeSpace[i] - positionRelativeToCamera.xyz);				// Per-Pixel Lighting
	}																												// Per-Pixel Lighting

	toCameraVector = toTangentSpace * (-positionRelativeToCamera.xyz);												// Specular Lighting

	float distanceFromCamera = length(positionRelativeToCamera.xyz);												// Fog
	visibility = exp(-pow((distanceFromCamera * densityOfFog), gradientOfFog));										// Fog
	visibility = clamp(visibility, 0.0, 1.0);																		// Fog

}
