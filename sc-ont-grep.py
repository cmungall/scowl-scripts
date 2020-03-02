#!/usr/bin/env python3
import click
import os

@click.command()
@click.option('-i', '--input')
@click.option('-o', '--output')
@click.argument('test')
def run(input, output, test):
    """Dumb wrapper."""
    c_arg = f'saveOntology(ontFilter("{input}", a => {{ a match {{ {test} }} }} ), "{output}")'
    cmd = f"amm -p predef.sc -c '{c_arg}'"
    print(cmd)
    os.system(cmd)
    

if __name__ == '__main__':
    run()
