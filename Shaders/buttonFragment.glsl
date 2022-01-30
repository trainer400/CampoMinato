#version 330 core

in vec2 textureCoordinates;

uniform sampler2D textureFile;

out vec4 fragColor;

void main()
{
	fragColor = texture(textureFile, textureCoordinates);
}
