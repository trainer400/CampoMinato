#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texture;

out vec2 textureCoordinates;

void main()
{
	//Make the texture coordinates go forward in the pipeline
	textureCoordinates.xy = texture.xy;

	//Assign the position in the window
	gl_Position = vec4(position, 0, 1);
}
