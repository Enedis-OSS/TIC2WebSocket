# Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
#
# SPDX-FileContributor: Jehan BOUSCH
# SPDX-FileContributor: Mathieu SABARTHES
#
# SPDX-License-Identifier: Apache-2.0

if($args.count -lt 1)
{
  Write-Error 'Usage: ' + $MyInvocation.MyCommand.Name + ' portName [ticMode]' -ErrorAction Stop
}
$portName = $args[0]
if($args.count -lt 2)
{
  $ticMode = "STANDARD"
}
else
{
	$ticMode = $args[1]
}

if($ticMode -eq "STANDARD")
{
  $baudrate = 9600
}
elseif($ticMode -eq "HISTORIC")
{
  $baudrate = 1200
}
else
{
  Write-Error 'The tic mode "' + $ticMode + '" is unknown (only "STANDARD" or "HISTORIC" is allowed)!' -ErrorAction Stop
}

$port = new-Object System.IO.Ports.SerialPort $portName,$baudrate,Even,7,one
$port.open()
while($port.IsOpen)
{
  $line = $port.ReadLine()
  Write-Output $line
}
$port.close()