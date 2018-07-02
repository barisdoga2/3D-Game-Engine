#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;						// Per-Pixel Lighting
in vec3 toLightVector;						// Per-Pixel Lighting
in vec3 toCameraVector;						// Specular Lighting
in float visibility;						// Fog


uniform sampler2D backgroundTexture;		// Multitexturing
uniform sampler2D rTexture;					// Multitexturing
uniform sampler2D gTexture;					// Multitexturing
uniform sampler2D bTexture;					// Multitexturing
uniform sampler2D blendMapTexture;			// Multitexturing

uniform vec3 lightColor;					// Per-Pixel Lighting
uniform float shineDamper;					// Specular Lighting
uniform float reflectivity;					// Specular Lighting
uniform vec3 skyColor;						// Fog

out vec4 out_Color;

void main(){
	
	vec4 blendMapColor = texture(blendMapTexture, pass_textureCoords);											// Multitexturing
	float backgroundTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);					// Multitexturing
	vec2 tiledTextureCoords = pass_textureCoords * 20;														// Multitexturing
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledTextureCoords) * backgroundTextureAmount;		// Multitexturing
	vec4 rTextureColor = texture(rTexture, tiledTextureCoords) * blendMapColor.r;								// Multitexturing
	vec4 gTextureColor = texture(gTexture, tiledTextureCoords) * blendMapColor.g;								// Multitexturing
	vec4 bTextureColor = texture(bTexture, tiledTextureCoords) * blendMapColor.b;								// Multitexturing
	vec4 totalTextureColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;			// Multitexturing
	
	vec3 normalizedSurfaceNormal = normalize(surfaceNormal);													// Per-Pixel Lighting - Specular Lighting
	vec3 normalizedToLightVector = normalize(toLightVector);													// Per-Pixel Lighting
	vec3 normalizedToCameraVector = normalize(toCameraVector);													// Specular Lighting
	
	float dotProduct = dot(normalizedSurfaceNormal, normalizedToLightVector);									// Per-Pixel Lighting
	float brightness = max(dotProduct, 0.2);																	// Per-Pixel Lighting
	vec3 diffuse = brightness * lightColor;																		// Per-Pixel Lighting
	
	vec3 lightDirection = -normalizedToLightVector;																// Specular Lighting
	vec3 reflectedLightDirection = reflect(lightDirection, normalizedSurfaceNormal);							// Specular Lighting
	float specularFactor = dot(reflectedLightDirection, normalizedToCameraVector);								// Specular Lighting
	specularFactor = max(specularFactor, 0.0);																	// Specular Lighting
	float dampedFactor = pow(specularFactor, shineDamper);														// Specular Lighting
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;												// Specular Lighting
	
	
	out_Color = vec4(diffuse, 1.0) * totalTextureColor + vec4(finalSpecular, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
	
}